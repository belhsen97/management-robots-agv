import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Store } from '@ngrx/store';
import { getValueSearchInput } from 'src/app/core/store/selectors/Global.selector';
import { WorkstationService } from 'src/app/core/services/workstation.service';
import { MatDialog } from '@angular/material/dialog';
import { MessageBoxConfirmationComponent } from '../../shared/message-box-confirmation/message-box-confirmation.component';
import { TagDto } from 'src/app/core/store/models/Tag/TagDto.model';
import { AddTagComponent } from '../add-tag/add-tag.component';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { ShowAlert } from 'src/app/core/store/actions/Global.Action';
import { TagService } from 'src/app/core/services/tag.service';
import { tagState } from 'src/app/core/store/states/Tag.state';
import { wsState } from 'src/app/core/store/states/Worstation.state';
import { GlobalState } from 'src/app/core/store/states/Global.state';

@Component({
  selector: 'app-list-tags',
  templateUrl: './list-tags.component.html',
  styleUrls: ['./list-tags.component.css']
})
export class ListTagsComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild("paginatorTag") paginatorTag  !: MatPaginator;
  @ViewChild(MatSort) sortTag   !: MatSort;
  displayedColumnsTag: string[] = ['code', 'workstation', 'action'];

  dataSource: MatTableDataSource<TagDto> = new MatTableDataSource<TagDto>();

  constructor(private storeGlobal: Store<GlobalState>,
    public workstationService: WorkstationService,
    public tagService: TagService,
    private dialog: MatDialog) { }

    
  ngOnInit(): void {
    // this.store.select(getrouterinfo).subscribe(item => {
    //   console.log( item);
    // });
    this.storeGlobal.select(getValueSearchInput).subscribe(value => {
      if (value === null || value === undefined || this.dataSource == undefined) { return; }
      this.dataSource.filter = value;
      console.log(value);
    });

    this.workstationService.getAll().subscribe(
      (response) => {
        wsState.listWorkstations = response.body;
      });

    this.dataSource = new MatTableDataSource<TagDto>(tagState.listTags);
    this.tagService.getAll().subscribe(
      (response) => {
        tagState.listTags = response.body;
        this.dataSource.data = tagState.listTags;
      });
  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginatorTag;
    this.dataSource.sort = this.sortTag;
  }
  ngOnDestroy(): void {
    if (this.dataSource) {
      this.dataSource.disconnect();
    }
  }

  openPopupTag(element: any, title: any, isedit = false) {
    const dialogRef = this.dialog.open(AddTagComponent, {
      width: '40%',
      data: {
        element: element,
        title: title,
        isedit: isedit
      }
    })
    return dialogRef;
  }
  //Msg box
  openDialogConfirmation(title: string, message: string, enterAnimationDuration: string,
    exitAnimationDuration: string, callback: () => void): void {
    const dialogRef = this.dialog.open(MessageBoxConfirmationComponent, {
      width: '400px',
      // height: '400px',
      data: { title: title, message: message },
      enterAnimationDuration,
      exitAnimationDuration,
      disableClose: false
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        callback();
      }
    });
  }

  onChangeStatus(id: any, event: any) {
    const selectedStatus = event.value;
    console.log(id, selectedStatus);
  }

  onClickAdd(): void {
    const dialogRef = this.openPopupTag(undefined, "Add New Tag");
    dialogRef.afterClosed().subscribe(result => {
      if (result == null) { return; }
      this.tagService.insert(result).subscribe(
        (response) => {
          tagState.tag = response.body;
          this.tagService.msgResponseStatus = { title: "Message", datestamp: new Date(), status: ReponseStatus.SUCCESSFUL, message: "success add new workstation" };
          this.storeGlobal.dispatch(ShowAlert(this.tagService.msgResponseStatus));
          tagState.listTags.push(tagState.tag);
          this.dataSource.data = tagState.listTags;
        }
      );
    });
  }
  onClickEdit(tag: any): void {
    const dialogRef = this.openPopupTag(tag, "Edit Tag", true);
    dialogRef.afterClosed().subscribe(result => {
      if (result == null) { return; }
      this.tagService.update(tag.id, result).subscribe(
        (response) => {
          tagState.tag = response.body;
          this.tagService.msgResponseStatus = { title: "Message", datestamp: new Date(), status: ReponseStatus.SUCCESSFUL, message: "success add new workstation" };
          this.storeGlobal.dispatch(ShowAlert(this.tagService.msgResponseStatus));

          const index = tagState.listTags.findIndex(tag => tag.id === tagState.tag.id);
          if (index !== -1) { tagState.listTags[index] = tagState.tag; }
          //   this.tagService.listTags.forEach((w) => { if(w.id === id) { w =   this.tagService.workstation ;} });

          this.dataSource.data = tagState.listTags;
        }
      );
    });
  }

  onClickDelete(id: any): void {
    this.openDialogConfirmation('Confirmation', '  Would you want to delete Tag equal ' + id + ' ?', '300ms', '500ms',
      () => {
        this.tagService.delete(id).subscribe(
          (response) => {
            this.tagService.msgResponseStatus = response.body;
            this.storeGlobal.dispatch(ShowAlert(this.tagService.msgResponseStatus));
            tagState.listTags = tagState.listTags.filter(item => item.id !== id);
            this.dataSource.data = tagState.listTags;
          }
        );
      });
  }
}
