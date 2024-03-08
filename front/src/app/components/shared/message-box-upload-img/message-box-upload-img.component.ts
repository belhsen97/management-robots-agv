import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-message-box-upload-img',
  templateUrl: './message-box-upload-img.component.html',
  styleUrls: ['./message-box-upload-img.component.css']
})
export class MessageBoxUploadImgComponent implements OnInit  {
  spanBigChar : any = null;

constructor() { }
ngOnInit(): void {   this.spanBigChar = document.getElementById('span-first-char');
if (this.text.length > 0) {this.spanBigChar.innerHTML = this.text[0];this.text=this.text.slice(1);} 
} 

@Input() state : boolean = false;
@Input() title!: string;
@Input() text!: string;
@Output() onYesNoEvent=new EventEmitter<boolean>(); 
@Output() fileValidate=new EventEmitter<File>(); 
file?: File ; // Variable to store file


onClickYesNo(state : boolean):void { 
  this.state = state;  
  this.onYesNoEvent.emit(this.state); 
} 


 onClickToSend():void { 
   this.valdator ();
   if (this.valid){
   this.state = !this.valid;
   this.fileValidate.emit(this.file);
   this.onYesNoEvent.emit(this.state);}
  }

onChange($event:any) {
 this.file = $event.target.files[0]; 
 this.valdator ();
}

valid: boolean = false; 
typeInvalid : boolean = false;
requiredInvalid: boolean = false;
valdator():void {//   image/jpeg  image/jpg  image/png 
       if (this.file == undefined ){ this.requiredInvalid = true;  this.typeInvalid = false; this.valid = false; }
       else if ( !(this.file?.type ==  'image/jpeg' || this.file?.type ==  'image/jpg' || this.file?.type ==  'image/png'))
               {this.typeInvalid = true; this.valid = false; this.requiredInvalid = false;}
       else {this.valid = true; this.typeInvalid = false; this.requiredInvalid = false; }}







      /* onClickSend(form: NgForm):void {  
        if (!form.invalid){
             this.state = false;
             this.onYesNoEvent.emit(this.state);
             console.log(form.invalid);  //this.stateMsgBoxAuth = true; 
              console.log(this.file?.type); }}*/
}


