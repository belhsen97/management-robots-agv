import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-select-button-choice',
  templateUrl: './select-button-choice.component.html',
  styleUrls: ['./select-button-choice.component.css']
})
export class SelectButtonChoiceComponent {
  public typeProperty: string = "ALL";
  @Output() onChange=new EventEmitter<String>(); 
  onToggleButtonClick(): void {
    this.onChange.emit(  this.typeProperty);
  }
}
