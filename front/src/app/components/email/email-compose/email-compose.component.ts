import {COMMA, ENTER} from '@angular/cdk/keycodes'; 
import { LiveAnnouncer } from '@angular/cdk/a11y'; 
import { AfterViewInit, Component, ElementRef, OnInit, Renderer2, ViewChild, inject } from '@angular/core';
import { FormControl, NgForm } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatChipInputEvent } from '@angular/material/chips';
import { Observable, map, startWith } from 'rxjs';
import FroalaEditor from 'froala-editor';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { MailClientService } from 'src/app/core/services/mail-client.service';
import { MailState, mailState } from 'src/app/core/store/states/Mail.state';
import { Store } from '@ngrx/store';
import { GlobalState } from 'src/app/core/store/states/Global.state';
import { ShowAlert } from 'src/app/core/store/actions/Global.Action';
import { ReponseStatus } from 'src/app/core/store/models/Global/ReponseStatus.enum';
import { MsgResponseStatus } from 'src/app/core/store/models/Global/MsgResponseStatus.model';
import { RecipientType } from 'src/app/core/store/models/mail/RecipientType.enum';
import { TypeBody } from 'src/app/core/store/models/mail/TypeBody.enum';

@Component({
  selector: 'app-email-compose',
  templateUrl: './email-compose.component.html',
  styleUrls: ['./email-compose.component.css'],
    providers: [ ]
})
export class EmailComposeComponent  implements OnInit , AfterViewInit {
    //@ViewChild('valueTemplate', { static: true })  rteObj !: RichTextEditorComponent ; 
  selectedFiles: File[] = [];
  elementFloara !: any;

  public options: Object = {
    charCounterCount: true,
    placeholderText: 'Edit Your Content Here!',
    toolbarButtons: ['bold', 'italic', 'underline', 'paragraphFormat','alert'],
    toolbarButtonsXS: ['bold', 'italic', 'underline', 'paragraphFormat','alert'],
    toolbarButtonsSM: ['bold', 'italic', 'underline', 'paragraphFormat','alert'],
    toolbarButtonsMD: ['bold', 'italic', 'underline', 'paragraphFormat','alert'],
      events : {
          'FroalaEditor.contentChanged' : function(){
            console.log('Content updated!');
          }
        }
  };


  

 public mailState! : MailState;
  constructor(private sanitizer: DomSanitizer  , private mailService : MailClientService, private storeGlobal: Store<GlobalState>) {
    this.mailState  =  mailState;
  }

  ngOnInit(): void { 
    this.mailService.getAllAddressMail().subscribe(
      (response) => {  mailState.listAddress = response.body; }
      ,(error) => {
         this.storeGlobal.dispatch( ShowAlert(
          { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
        ) ); 
      }) ;




    // let rteValue: string = this.rteObj.value;
    // let rteValue: string = "AAAA";
    // console.log(rteValue);
    // this.rteObj.value = "AAAAAAAAAAAAAAAAAAAA";

    // const trialMessageElement = document.querySelector('div[style*="position: fixed"]');
    // if (trialMessageElement) { trialMessageElement.remove();}

    this.elementFloara = new FroalaEditor('div#editor-div', {}, () => {
      // Ensure that the Froala Editor is initialized
      this.elementFloara.html.set('<p>New HTML content</p>');
      //  console.log(editor.html.get())
    });

  }
  ngAfterViewInit(): void {
  
  }



  srcResult : any ; 
  onChangeFiles(event:any):void{
    if (event.target.files && event.target.files.length > 0) {
      this.selectedFiles = Array.from(event.target.files);
    }
    console.log ( event.target.files.length);

    // const inputNode: any = document.querySelector('#file');
  
    // if (typeof (FileReader) !== 'undefined') {
    //   const reader = new FileReader();
  
    //   reader.onload = (e: any) => {
    //     this.srcResult = e.target.result;
    //   };
  
    //   reader.readAsArrayBuffer(inputNode.files[0]);
    // }
  }
  
  public editorContent: string = 'AAAAAAAAAAAAAAAAAAAAAAA'
 
 
  safeHtml!: SafeHtml;
  onSubmitForm(form: NgForm): void { if (!form.invalid) {   }
  console.log((form.invalid ? "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" : "bbbbbbbbbbbbbbbbbbbbbbbb" ) )
  const htmlContent = this.elementFloara.html.get();
  // console.log(htmlContent); 
  // let rteValue: string = this.rteObj.value;
  // let rteValue: any = this.rteObj.getHtml();
  // console.log(rteValue.length);
  // this.safeHtml = this.sanitizer.bypassSecurityTrustHtml(this.rteObj.getHtml());
  this.mailState.msg.timestamp = new Date();
  this.mailState.msg.bodyContents = [];
  this.mailState.msg.bodyContents.push({content: htmlContent,type:TypeBody.HTML});
  this.mailState.msg.recipients = [];
  this.mailState.recipients.to.forEach((r)=>{this.mailState.msg.recipients.push({ type :RecipientType.TO  ,  address :  r});});
  this.mailState.recipients.cc.forEach((r)=>{this.mailState.msg.recipients.push({ type :RecipientType.CC  ,  address :  r});});
  this.mailState.recipients.bcc.forEach((r)=>{this.mailState.msg.recipients.push({type :RecipientType.BCC ,  address :  r});});
 console.log(this.mailState.msg);

  // this.mailService.sendMail(this.selectedFiles,this.mailState.msg).subscribe(
  //   (response) => { 
  //     const msg : MsgResponseStatus = response.body;  
  //     this.storeGlobal.dispatch( ShowAlert( msg ) ); 
  //   }
  //   ,(error) => {
  //      this.storeGlobal.dispatch( ShowAlert(
  //       { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
  //     ) ); 
  //   }) ;
}
 
outputListMailTo($event : String[]):void {this.mailState.recipients.to = $event;}
outputListMailCC($event : String[]):void {this.mailState.recipients.cc = $event;}
outputListMailBCC($event : String[]):void {this.mailState.recipients.bcc = $event;}
}


// <!-- <mat-form-field class="example-chip-list col-sm-10">
// <mat-label>Mails</mat-label>
// <mat-chip-grid #chipGrid aria-label="Fruit selection">
//    <mat-chip-row *ngFor="let destinate of recipientsTo" (removed)="remove(destinate)">
//       {{destinate}}
//       <button matChipRemove [attr.aria-label]="'remove ' + destinate">
//          <mat-icon>cancel</mat-icon>
//       </button>
//    </mat-chip-row>
// </mat-chip-grid>
// <input placeholder="Add Mail..." #fruitInput matInput name="fruitInput"
//    [matChipInputFor]="chipGrid" [matAutocomplete]="auto"
//    [matChipInputSeparatorKeyCodes]="separatorKeysCodes" (matChipInputTokenEnd)="add($event)"
//    [(ngModel)]="newDestinate" />
// <mat-autocomplete #auto="matAutocomplete" (optionSelected)="selected($event)">
//    <mat-option *ngFor="let fruit of filterFruits(newDestinate)"
//       [value]="fruit">{{fruit}}</mat-option>
// </mat-autocomplete>
// </mat-form-field> -->
  // recipientsTo: string[] = ['Lemon@gmail.com'];
  //newDestinate: string = '';
  // separatorKeysCodes: number[] = [ENTER, COMMA];
  // @ViewChild('fruitInput') fruitInput!: ElementRef<HTMLInputElement>;
  // announcer = inject(LiveAnnouncer);
  // add(event: MatChipInputEvent): void {
  //   const value = (event.value || '').trim();
  //   if (value && !this.recipientsTo.includes(value)) {
  //     this.recipientsTo.push(value);
  //     this.announcer.announce(`Added ${value}`);
  //   }
  //   event.chipInput!.clear();
  //   this.newDestinate = '';
  // }
  // remove(mail: string): void {
  //   const index = this.recipientsTo.indexOf(mail);
  //   if (index >= 0) {
  //     this.recipientsTo.splice(index, 1);
  //     this.announcer.announce(`Removed ${mail}`);
  //   }
  // }
  // selected(event: MatAutocompleteSelectedEvent): void {
  //   const value = event.option.viewValue;
  //   // if (!this.fruits.includes(value)) {
  //     this.recipientsTo.push(value);
  //     this.fruitInput.nativeElement.value = '';
  //     this.newDestinate = '';// }
  // } 
  // filterFruits(value: string): String[] {
  //   const addressValue = value.toLowerCase();
  //   return mailState.listAddress.filter(address => address.toLowerCase().includes(addressValue));
  // }