import {HttpClient} from '@angular/common/http';
import {ChangeDetectionStrategy, Component, forwardRef, Input, OnInit} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {FileLikeObject, FileUploader} from 'ng2-file-upload';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {isArray, isNullOrUndefined} from 'util';
import {baseApiUrl} from '../../../../environments/environment';
import {UtilService} from '../../../core/services/util.service';
import {UploadFile} from '../../../shared/models/upload-file';
import {UploadService} from '../../services/upload.service';

@Component({
  selector: 'jwjh-file-input',
  templateUrl: './file-input.component.html',
  styleUrls: ['./file-input.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => FileInputComponent),
    multi: true
  }],
})
export class FileInputComponent implements OnInit, ControlValueAccessor {
  uploadFiles$ = new BehaviorSubject<UploadFile[]>([]);
  uploader = new FileUploader({url: `${baseApiUrl}/uploads`});
  @Input()
  img2docx = false;
  @Input()
  btnTxt = 'COMMON.UPLOAD';
  @Input()
  btnTooltip = 'TOOLTIP.UPLOAD';
  @Input()
  multiple = true;
  @Input()
  allowedMimeType: string[];
  @Input()
  allowedFileType: string[];
  onModelChange: Function = () => {
  }
  onModelTouched: Function = () => {
  }
  private MAX_FILE_SIZE = 10 * 1024 * 1024;
  @Input()
  maxFileSize = this.MAX_FILE_SIZE;

  constructor(private http: HttpClient,
              private utilService: UtilService,
              private uploadService: UploadService) {
  }

  ngOnInit(): void {
    this.maxFileSize = this.maxFileSize || this.MAX_FILE_SIZE;
    this.maxFileSize = this.maxFileSize > this.MAX_FILE_SIZE ? this.MAX_FILE_SIZE : this.maxFileSize;
    const options = Object.assign({}, this.uploader.options, {
      maxFileSize: this.maxFileSize,
      allowedMimeType: this.allowedMimeType,
      allowedFileType: this.allowedFileType,
    });
    this.uploader.setOptions(options);
    this.uploader.onWhenAddingFileFailed = (item: FileLikeObject, filter: any, opt: any) => {
      if (filter.name === 'fileSize') {
        this.utilService.showError('ERROR_CODE.FILESIZE');
      } else {
        this.utilService.showError('ERROR_CODE.FILETYPE');
      }
    };
    this.uploader.onAfterAddingFile = fileItem => {
      if (this.img2docx) {
        if (/\.(jpe?g|gif|png|bmp|webp)$/i.test(fileItem.file.name)) {
          fileItem.url = `${baseApiUrl}/uploads/editors2world`;
        }
      }
      fileItem.withCredentials = false;
      fileItem.onSuccess = s => {
        const uploadFile = JSON.parse(s);
        if (this.img2docx) {
          this.add(uploadFile);
        } else {
          this.uploadService.updateFileName(uploadFile.id, fileItem.file.name).subscribe(res => {
            this.add(res);
          });
        }
      };
      fileItem.onError = res => this.utilService.showError(res);
      fileItem.upload();
    };
  }

  paste(ev: ClipboardEvent) {
    ev.preventDefault();
    ev.stopPropagation();
    const items = ev.clipboardData && ev.clipboardData.items;
    if (!items) {
      alert('图片不存在，请先复制图片！');
      return;
    }
    for (let i = 0; i < items.length; i++) {
      if (items[i].type.indexOf('image') !== -1) {
        const imageFile = items[i].getAsFile();
        const reader = new FileReader();
        reader.onload = (e: any) => this.base64Image(e.target.result);
        reader.readAsDataURL(imageFile);
      }
    }
  }

  add(uploadFile: UploadFile) {
    const next = [uploadFile].concat(this.uploadFiles$.value || []);
    this.handleChange(next);
  }

  remove(uploadFile: UploadFile) {
    this.utilService.showConfirm().subscribe(() => {
      const next = (this.uploadFiles$.value || []).filter(it => it.id !== uploadFile.id);
      this.handleChange(next);
    });
  }

  writeValue(value: any): void {
    if (isNullOrUndefined(value)) {
      this.uploadFiles$.next([]);
    } else if (isArray(value)) {
      this.uploadFiles$.next(value);
    } else {
      this.uploadFiles$.next([value]);
    }
  }

  registerOnChange(fn: any): void {
    this.onModelChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onModelTouched = fn;
  }

  private base64Image(base64: string) {
    this.http.post(`${baseApiUrl}/uploads/base64Image2world`, base64)
      .subscribe((it: UploadFile) => this.add(it));
  }

  private handleChange(next: UploadFile[]) {
    if (this.multiple) {
      this.uploadFiles$.next(next);
      this.onModelChange(next);
    } else {
      next = next.slice(0, 1);
      this.uploadFiles$.next(next);
      this.onModelChange(next[0]);
    }
  }
}
