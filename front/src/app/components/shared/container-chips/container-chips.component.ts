import { LiveAnnouncer } from '@angular/cdk/a11y';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { Component, ElementRef, EventEmitter, Input, Output, ViewChild, inject } from '@angular/core';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatChipInputEvent } from '@angular/material/chips';

@Component({
  selector: 'app-container-chips',
  templateUrl: './container-chips.component.html',
  styleUrls: ['./container-chips.component.css']
})
export class ContainerChipsComponent    {
  @ViewChild('chipGridInput') chipGridInput!: ElementRef<HTMLInputElement>;
  announcer = inject(LiveAnnouncer);

  separatorKeysCodes: number[] = [ENTER, COMMA];

  @Input() public label:String = '';
  @Input()  listDefine:String[] = [];
  listOut: string[] = [];
  @Output() onChange=new EventEmitter<string[]>(); 
  
  value: string = '';

  constructor() {} 

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();
    if (value && !this.listOut.includes(value)) {
      this.listOut.push(value);
      this.announcer.announce(`Added ${value}`);
      this.onChange.emit(  this.listOut);
    }
    event.chipInput!.clear();
    this.value = '';
  }
  remove(mail: string): void {
    const index = this.listOut.indexOf(mail);
    if (index >= 0) {
      this.listOut.splice(index, 1);
      this.announcer.announce(`Removed ${mail}`);
      this.onChange.emit(  this.listOut);
    }
  }
  selected(event: MatAutocompleteSelectedEvent): void {
    const value = event.option.viewValue;
    // if (!this.fruits.includes(value)) {
      this.listOut.push(value);
      this.chipGridInput.nativeElement.value = '';
      this.value = '';// }
      this.onChange.emit(  this.listOut);
  } 
  filterFruits(value: string): String[] {
    const addressValue = value.toLowerCase();
    return this.listDefine.filter(element => element.toLowerCase().includes(addressValue));
  }
}
