import {ChangeDetectionStrategy, Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {DomSanitizer} from '@angular/platform-browser';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {of} from 'rxjs/observable/of';
import {baseApiUrl} from '../../../../environments/environment';
import {isImage, isVideo, isWord} from '../../../core/services/util.service';
import {UploadFile} from '../../../shared/models/upload-file';
import {UploadService} from '../../services/upload.service';

@Component({
  selector: 'jwjh-file-show-dialog',
  templateUrl: './file-show-dialog.component.html',
  styleUrls: ['./file-show-dialog.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FileShowDialogComponent {
  readonly file: UploadFile;
  readonly downloadToken: string;
  readonly downloadTokenByDocx: string;
  readonly downloadTokenByAnswer: string;
  readonly downloadTokenByQuestion: string;
  readonly title: string;
  readonly isHtml: boolean;
  readonly html$: Observable<string>;
  readonly isVideo: boolean;
  readonly videoHSrc: string;
  readonly videoH: number;

  constructor(private store: Store<any>,
              private sanitizer: DomSanitizer,
              private uploadService: UploadService,
              private dialogRef: MatDialogRef<FileShowDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    if (data.openByJoin) {
      this.downloadToken = data.downloadToken;
      this.downloadTokenByDocx = data.downloadTokenByDocx;
      this.downloadTokenByQuestion = data.downloadTokenByQuestion;
      this.downloadTokenByAnswer = data.downloadTokenByAnswer;
      this.title = data.fileName;
      this.isHtml = true;
      this.html$ = of(sanitizer.bypassSecurityTrustHtml(data.html));
    } else {
      this.file = data.file;
      this.downloadToken = this.file.downloadToken;
      this.title = data.title || this.file.fileName;
      this.isHtml = isWord(this.file);
      if (this.isHtml) {
        this.html$ = this.uploadService.getFileHtml(this.downloadToken).map(res => sanitizer.bypassSecurityTrustHtml(res.data));
      }
      this.videoHSrc = `${baseApiUrl}/opens/downloads/${this.downloadToken}/video`;
      this.videoH = window.innerHeight * 0.5;
      this.isVideo = isVideo(this.file);
    }
  }

  static open(dialog: MatDialog, file: UploadFile, title?: string) {
    if (isImage(file)) {
      const url = `${baseApiUrl}/opens/downloads/${file.downloadToken}/image`;
      if (file.fileSize > 3 * 1024) {
        window.open(url + '/w/1208');
      } else {
        window.open(url);
      }
    } else if (isVideo(file) || isWord(file)) {
      dialog.open(FileShowDialogComponent, {
        disableClose: true,
        panelClass: 'my-dialog',
        data: {file, title}
      });
    } else {
      window.open(`${baseApiUrl}/opens/downloads/${file.downloadToken}`);
    }
  }

  static openByJoin(dialog: MatDialog, res: { fileName: string, downloadToken: string, html: string }) {
    dialog.open(FileShowDialogComponent, {
      disableClose: true,
      panelClass: 'my-dialog',
      data: {...res, openByJoin: true}
    });
  }

  download() {
    window.open(`${baseApiUrl}/opens/downloads/${this.downloadToken}`);
  }

  downloadByDocx() {
    window.open(`${baseApiUrl}/opens/downloads/${this.downloadTokenByDocx}`);
  }

  downloadByQuestion() {
    window.open(`${baseApiUrl}/opens/downloads/${this.downloadTokenByQuestion}`);
  }

  downloadByAnswer() {
    window.open(`${baseApiUrl}/opens/downloads/${this.downloadTokenByAnswer}`);
  }
}
