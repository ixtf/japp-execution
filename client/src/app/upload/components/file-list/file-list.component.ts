import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {MatDialog} from '@angular/material';
import {Store} from '@ngrx/store';
import {baseApiUrl} from '../../../../environments/environment';
import {UploadFile} from '../../../shared/models/upload-file';
import {UploadService} from '../../services/upload.service';
import {FileShowDialogComponent} from '../file-show-dialog/file-show-dialog.component';

@Component({
  selector: 'jwjh-file-list',
  templateUrl: './file-list.component.html',
  styleUrls: ['./file-list.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FileListComponent {
  @Input()
  files: UploadFile[];
  @Input()
  isShowHead: boolean;

  constructor(private store: Store<any>,
              private dialog: MatDialog,
              private uploadService: UploadService) {
  }

  get count(): number {
    return this.files ? this.files.length : 0;
  }

  clickFile(file: UploadFile) {
    FileShowDialogComponent.open(this.dialog, file);
  }

  download(file: UploadFile) {
    window.open(`${baseApiUrl}/opens/downloads/${file.downloadToken}`);
  }

}
