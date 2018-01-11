import {ChangeDetectionStrategy, Component, HostListener, Input} from '@angular/core';

@Component({
  moduleId: module.id,
  selector: 'jwjh-editor-view',
  templateUrl: './editor-view.component.html',
  styleUrls: ['./editor-view.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditorViewComponent {
  @Input()
  content: string;

  @HostListener('click', ['$event'])
  private click(ev: Event) {
    const src = $(ev.target).attr('src');
    if (src) {
      window.open(src);
    }
  }
}
