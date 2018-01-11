import {
  ChangeDetectionStrategy, Component, ElementRef, HostListener, Input, OnChanges, OnDestroy,
  SimpleChanges
} from '@angular/core';
import * as echarts from 'echarts';
import {isNullOrUndefined} from 'util';

@Component({
  selector: 'jwjh-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ChartComponent implements OnChanges, OnDestroy {
  @Input()
  options: any;
  @Input()
  w: number;
  @Input()
  h = 200;
  private chart;

  constructor(private elementRef: ElementRef) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    const ele = this.elementRef && this.elementRef.nativeElement;
    if (!this.chart) {
      this.chart = echarts.init(ele);
    }

    const optionsChange = changes.options;
    if (optionsChange && this.options) {
      this.chart.setOption(this.options);
    }
  }

  ngOnDestroy(): void {
    if (this.chart) {
      this.chart.dispose();
    }
  }

  @HostListener('window:resize')
  private windowResize() {
    if (this.chart) {
      setTimeout(() => {
        const ele = this.elementRef && this.elementRef.nativeElement;
        const bcr = ele && ele.getBoundingClientRect();
        const width = isNullOrUndefined(this.w) ? bcr.width : this.w;
        this.chart.resize({width, height: this.h});
      }, 500);
    }
  }

}
