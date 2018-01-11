import {NgModule} from '@angular/core';
import {FileUploadModule} from 'ng2-file-upload';
import {SharedModule} from '../shared/shared.module';
import {FileInputComponent} from './components/file-input/file-input.component';
import {FileListComponent} from './components/file-list/file-list.component';
import {FileShowDialogComponent} from './components/file-show-dialog/file-show-dialog.component';
import {FaFileIconPipe} from './services/fa-file-icon.pipe';
import {UploadService} from './services/upload.service';

export const ENTRYCOMPONENTS = [
  FileShowDialogComponent,
];
export const COMPONENTS = [
  FaFileIconPipe,
  FileInputComponent,
  FileListComponent,
  ...ENTRYCOMPONENTS
];

@NgModule({
  imports: [
    SharedModule,
    FileUploadModule,
  ],
  entryComponents: ENTRYCOMPONENTS,
  declarations: COMPONENTS,
  exports: COMPONENTS,
  providers: [
    UploadService
  ]
})
export class UploadModule {
}
