import {Entity} from './entity';

export const FIELD_TYPES = [
  {value: 'BOOLEAN', name: '布尔'},
  {value: 'NUMBER_LONG', name: '整数'},
  {value: 'NUMBER_DOUBLE', name: '小数'},
  {value: 'TEXT', name: '文本'},
  {value: 'SELECT_SINGLE', name: '单选'},
  {value: 'SELECT_MULTIPLE', name: '多选'},
  // {value: 'RICH_TEXT', name: '富文本'},
];

export class Field extends Entity {
  name: string;
  type: string;
  required: boolean;
  selectOptions: string[];
}
