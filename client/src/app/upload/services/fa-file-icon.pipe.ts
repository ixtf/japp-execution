import {Pipe, PipeTransform} from '@angular/core';
import {UploadFile} from '../../shared/models/upload-file';

export const FaFileTypes = [
  {reg: /\.(jpe?g|gif|png|bmp|webp)$/i, type: 'image', icon: 'fa-file-image-o', showImage: true},
  {reg: /\.xlsx?$/i, type: 'excel', icon: 'fa-file-excel-o', showBase: true},
  {reg: /\.docx?$/i, type: 'word', icon: 'fa-file-word-o', showBase: true},
  {reg: /\.pptx?$/i, type: 'powerpoint', icon: 'fa-file-powerpoint-o', showBase: true},
  {reg: /\.pdf$/i, type: 'pdf', icon: 'fa-file-pdf-o', showPdf: true},
  {reg: /\.(txt)$/i, type: 'text', icon: 'fa-file-text-o'},
  {reg: /\.(zip|rar|7z)$/i, type: 'archive', icon: 'fa-file-archive-o'},
  {reg: /\.(wav|mp3)$/i, type: 'audio', icon: 'fa-file-audio-o'},
  {reg: /\.(avi|mp4)$/i, type: 'video', icon: 'fa-file-video-o', showVideo: true}
];
export const defaultFaIcon = 'fa-file-o';

@Pipe({
  name: 'faFileIcon'
})
export class FaFileIconPipe implements PipeTransform {

  transform(uploadFile: UploadFile): string {
    if (uploadFile && uploadFile.fileName) {
      const ft = FaFileTypes.find(it => it.reg.test(uploadFile.fileName));
      return ft && ft.icon || defaultFaIcon;
    }
    return defaultFaIcon;
  }

}
