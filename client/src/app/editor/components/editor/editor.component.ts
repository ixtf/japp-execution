import {AfterContentChecked, ChangeDetectionStrategy, Component, EventEmitter, forwardRef, Input} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {baseApiUrl} from '../../../../environments/environment';

@Component({
  selector: 'jwjh-editor',
  templateUrl: './editor.component.html',
  styleUrls: ['./editor.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => EditorComponent),
    multi: true
  }],
})
export class EditorComponent implements ControlValueAccessor, AfterContentChecked {
  content$ = new EventEmitter<string>();
  @Input()
  theme: string;
  @Input()
  placeholder: string;
  @Input()
  options: any;
  onModelChange: Function = () => {
  }
  onModelTouched: Function = () => {
  }
  private tbs = ['bold', 'italic', 'fontFamily', 'fontSize', '|',
    'align', 'formatOL', 'formatUL', '|',
    'insertLink', 'insertImage', 'insertTable', '|',
    'insertHR'];
  private tbs_sm = ['align', 'formatOL', 'formatUL', '|', 'insertLink', 'insertImage'];
  private CONFIG = {
    language: 'zh_cn',
    imageUploadURL: baseApiUrl + '/uploads/editors',
    toolbarButtons: this.tbs,
    toolbarButtonsMD: this.tbs,
    toolbarButtonsSM: this.tbs_sm,
    toolbarButtonsXS: this.tbs_sm,
    toolbarSticky: false,
  };

  get config() {
    let res: any = Object.assign({}, this.CONFIG);
    if (this.options) {
      res = Object.assign(res, this.options);
    }
    if (this.placeholder) {
      res = Object.assign(res, {placeholderText: this.placeholder});
    }
    return res;
  }

  handleChanged(content: any): void {
    this.onModelChange(content);
  }

  writeValue(content: string): void {
    this.content$.emit(content);
  }

  registerOnChange(fn: any): void {
    this.onModelChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onModelTouched = fn;
  }

  ngAfterContentChecked(): void {
    setTimeout(() => {
      $('.fr-popup').css('z-index', '9995');
      // window['$']('.fr-placeholder').css('margin-top', '0');
    }, 2000);
  }


}
