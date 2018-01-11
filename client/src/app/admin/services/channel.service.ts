import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import {CurdService} from '../../core/services/curd.service';
import {Channel} from '../../shared/models/channel';

@Injectable()
export class ChannelService extends CurdService<Channel, string> {

  constructor(http: HttpClient, private store: Store<any>) {
    super(http, 'admin/channels');
  }
}
