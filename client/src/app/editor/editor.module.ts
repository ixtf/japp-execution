import {NgModule} from '@angular/core';
import {FroalaEditorModule, FroalaViewModule} from 'angular-froala-wysiwyg';

import {SharedModule} from '../shared/shared.module';
import {EditorViewComponent} from './components/editor-view/editor-view.component';
import {EditorComponent} from './components/editor/editor.component';

export const COMPONENTS = [
  EditorComponent,
  EditorViewComponent,
];

@NgModule({
  imports: [
    SharedModule,
    FroalaEditorModule.forRoot(),
    FroalaViewModule.forRoot()
  ],
  declarations: COMPONENTS,
  exports: COMPONENTS,
})
export class EditorModule {
}
