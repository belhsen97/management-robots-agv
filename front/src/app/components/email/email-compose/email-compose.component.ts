import {COMMA, ENTER} from '@angular/cdk/keycodes'; 
import { LiveAnnouncer } from '@angular/cdk/a11y'; 
import { AfterViewInit, Component, ElementRef, OnInit, Renderer2, ViewChild, inject } from '@angular/core';
import { FormControl, NgForm } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatChipInputEvent } from '@angular/material/chips';
import { Observable, map, startWith } from 'rxjs';
import FroalaEditor from 'froala-editor';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Component({
  selector: 'app-email-compose',
  templateUrl: './email-compose.component.html',
  styleUrls: ['./email-compose.component.css'],
    providers: [ ]
})
export class EmailComposeComponent  implements OnInit , AfterViewInit {
 
 
  //@ViewChild('valueTemplate', { static: true })  rteObj !: RichTextEditorComponent ;


  separatorKeysCodes: number[] = [ENTER, COMMA];
  destinates: string[] = ['Lemon@gmail.com'];
  allDestinates: string[] = ['Apple@gmail.com', 'Lemon@gmail.com', 'Lime@gmail.com', 'Orange@gmail.com', 'Strawberry@gmail.com'];
  newDestinate: string = '';




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

  @ViewChild('fruitInput') fruitInput!: ElementRef<HTMLInputElement>;
  announcer = inject(LiveAnnouncer);


 elementFloara !: any;
  
  constructor(private sanitizer: DomSanitizer ) {}




 






  ngOnInit(): void { 
    // let rteValue: string = this.rteObj.value;
    let rteValue: string = "AAAA";
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
  onFileSelected() {
    const inputNode: any = document.querySelector('#file');
  
    if (typeof (FileReader) !== 'undefined') {
      const reader = new FileReader();
  
      reader.onload = (e: any) => {
        this.srcResult = e.target.result;
      };
  
      reader.readAsArrayBuffer(inputNode.files[0]);
    }
  }
  
  public editorContent: string = 'AAAAAAAAAAAAAAAAAAAAAAA'
 
 
  safeHtml!: SafeHtml;
  onSubmitForm(form: NgForm): void { if (!form.invalid) {  }
  const htmlContent = this.elementFloara.html.get();
      console.log(htmlContent); 
  
  //let rteValue: string = this.rteObj.value;
  // let rteValue: any = this.rteObj.getHtml();
  // console.log(rteValue.length);
  // this.safeHtml = this.sanitizer.bypassSecurityTrustHtml(this.rteObj.getHtml());
}











  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();
    if (value && !this.destinates.includes(value)) {
      this.destinates.push(value);
      this.announcer.announce(`Added ${value}`);
    }
    event.chipInput!.clear();
    this.newDestinate = '';
  }
  remove(mail: string): void {
    const index = this.destinates.indexOf(mail);
    if (index >= 0) {
      this.destinates.splice(index, 1);
      this.announcer.announce(`Removed ${mail}`);
    }
  }
  selected(event: MatAutocompleteSelectedEvent): void {
    const value = event.option.viewValue;
    // if (!this.fruits.includes(value)) {
      this.destinates.push(value);
      this.fruitInput.nativeElement.value = '';
      this.newDestinate = '';// }
  } 
  filterFruits(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.allDestinates.filter(fruit => fruit.toLowerCase().includes(filterValue));
  }
}