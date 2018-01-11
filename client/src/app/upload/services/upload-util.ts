import {Injectable} from '@angular/core';
import {MatDialog} from '@angular/material';
import {UploadFile} from '../../shared/models/upload-file';

@Injectable()
export class Uploads {

  constructor(private dialog: MatDialog) {
  }

  showFile(file: UploadFile) {
  }
}
