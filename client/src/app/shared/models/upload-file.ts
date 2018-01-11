import {LogableEntity} from './logable-entity';

export class UploadFile extends LogableEntity {
  sha256Hex: string;
  fileName: string;
  fileSize: number;
  mediaType: string;
  downloadToken: string;
}
