import {ChangeDetectionStrategy, Component} from '@angular/core';

@Component({
  selector: 'jwjh-home',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HomePageComponent {

  constructor() {
  }

  test() {
    console.log(window['WeixinJSBridge']);
    const imgs = [];
    imgs.push('http://blog.zongdaosoft.com/uploadFiles/uploadImgs/17/1.jpg');
    window['WeixinJSBridge'].invoke('imagePreview', {
      'urls': imgs,
      'current': 'http://blog.zongdaosoft.com/uploadFiles/uploadImgs/17/1.jpg'
    });
  }
}
