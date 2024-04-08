import { AfterViewInit, ChangeDetectorRef, Component, Inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { RobotService } from 'src/app/core/services/robot.service';
import { WorkstationService } from 'src/app/core/services/workstation.service';
import { MqttClientService } from 'src/app/core/services/mqtt-client.service';

import * as Highcharts from 'highcharts/highstock';
import  dataInit  from 'highcharts/modules/data';
import highchartsMoreInit from 'highcharts/highcharts-more'; 
import  seriesLabelInit  from  'highcharts/modules/series-label';
import  exportingInit  from "highcharts/modules/exporting";
import  exportDataInit  from "highcharts/modules/export-data";
import  accessibilityInit  from "highcharts/modules/accessibility";
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Subscribe } from 'src/app/core/store/models/Mqtt/Subscribe.model';
import { IMqttMessage } from 'ngx-mqtt';
import { Subscription } from 'rxjs';
import { RobotDto } from 'src/app/core/store/models/Robot/RobotDto.model';
import { StatusRobot } from 'src/app/core/store/models/Robot/StatusRobot.enum';

dataInit(Highcharts);
highchartsMoreInit(Highcharts);
seriesLabelInit(Highcharts);
exportingInit(Highcharts);
exportDataInit (Highcharts);
accessibilityInit(Highcharts);

@Component({
  selector: 'app-gauge-chart',
  templateUrl: './gauge-chart.component.html',
  styleUrls: ['./gauge-chart.component.css']
})
export class GaugeChartComponent implements OnInit ,  AfterViewInit, OnDestroy {
  private curSubscription: Subscription | undefined;
  private chart : any ;
constructor( private mqttClientService :MqttClientService,
    private dialogRef: MatDialogRef<GaugeChartComponent>, 
      @Inject(MAT_DIALOG_DATA) private data: any){  }

ngOnInit() {  
  if (this.data.name == "") { this.dialogRef.close(null); return ;} 
   this.chart = Highcharts.chart("gauge-container-robot",this.chartOption);
  }



ngAfterViewInit() {
    const subscribe : Subscribe = {topic: "topic/robot/data/"+this.data.name,qos: 0};
    this.curSubscription = this.mqttClientService.subscribe(subscribe)?.subscribe((message: IMqttMessage) => {
        const  updateRobot : RobotDto =JSON.parse(message.payload.toString());
       console.log(updateRobot)
       this.chart.series[0].setData([parseFloat(updateRobot.speed.toFixed(1))]);
       this.chart.series[1].setData([ parseFloat(updateRobot.levelBattery.toFixed(1))]);
       const numCateegory = (updateRobot.statusRobot  ==  StatusRobot.RUNNING ? 1:  (updateRobot.statusRobot  ==  StatusRobot.WAITING ? 2 : 3 ) );
       this.chart.series[2].setData([numCateegory]);
     });
}
ngOnDestroy() {  this.mqttClientService.closeSubscribe(this.curSubscription);   this.chart.destroy(); }


//     <mat-card class="example-card col-md-6" *ngFor="let card of obs | async">
//      <div class="container-gauge"  #chartContainer  id="gauge-container-{{card.name}}"></div> 
// </mat-card>
//  this.robotService.getAll().subscribe(
//     (response) => { 
//       robotState.listRobots = response.body;
//       robotState.listRobots.forEach(r => r.createdAt = this.robotService.toDate(r.createdAt.toString()));
//       this.dataSource.data =    robotState.listRobots ;
//       this.observer = new IntersectionObserver((entries) => {
//         entries.forEach(entry => {
//           if (entry.isIntersecting) {
//             const chartId = entry.target.getAttribute("id");
//             this.loadChart(chartId!);
//             this.observer.unobserve(entry.target);
//           }
//         });
//       });
//       const chartContainers = document.querySelectorAll(".container-gauge");
//       chartContainers.forEach(container => {
//         this.observer.observe(container);
//       });
// observer!: IntersectionObserver;
// loadChart(id:string) :void{ Highcharts.chart(id,this.chartOption);}
//     }
//     ,(error) => {
//       this.robotService.msgResponseStatus  =   { title : "Error",   datestamp: new Date() ,status : ReponseStatus.ERROR , message : error.message}
//       this.store.dispatch( ShowAlert(  this.robotService.msgResponseStatus ) ); 
//       //this.robotService.goToComponent("/sign-in");
//     }) ;


 chartOption : any = {
  chart: {
      type: 'gauge',
      alignTicks: false,
      plotBackgroundColor: null,
      plotBackgroundImage: null,
      plotBorderWidth: 0,
      plotShadow: false
  },
  tooltip: {
      enabled: false
  },
  credits: {
    enabled: false
},
  title: {
      text: 'Robot- 1 '
  },
 pane: [{
      startAngle: -90,
      endAngle: 90,
          center: ['50%', '50%'],
           size: 300
  }, {
      startAngle: 110,
      endAngle: 250,
          center: ['50%', '50%'],
           size: 300
  }, {
      startAngle: 135,
      endAngle: 225,
          center: ['50%', '50%'],
           size: 300
  }
  ],
  yAxis: [{
     pane: 0,
      min: 0,
      max: 10,
      lineColor: '#000000',
      tickColor: '#202020',
      tickInterval: 1,
      tickWidth: 2,
      minorTickColor: '#339',
      offset: -5,
      lineWidth: 2,
      labels: {
          distance: -28,
          rotation: 'auto',
          color:'#202020',
           style: {  fontSize: '16px' }
      },
      tickLength: 10,
      minorTickLength: 8,
      endOnTick: false,
         plotBands: [{
          from: 8,
          to: 10,
          color: '#EE101F',
          innerRadius: '95%',
          outerRadius: '105%'
      },{
          from: 2,
          to: 8,
          color: '#28a745',
          innerRadius: '95%',
          outerRadius: '105%'
      },
      {
          from: 0,
          to: 2,
          color: '#EE101F',
          innerRadius: '96%',
          outerRadius: '105%'
      }
      ],
      title: {
          text: 'Powered by<br/>Highcharts',
          style: {
              color: '#BBB',
              fontWeight: 'normal',
              fontSize: '10px',
              lineHeight: '10px'
          },
          y: 10
      }
  }, {
     pane: 1,
      min: 0,
      max: 100,
      //tickPosition: 'outside',
      lineColor: '#000000',
      tickColor: '#202020',
      offset: -5,
      lineWidth: 2,
      //minorTickPosition: 'outside',
      tickInterval: 25,
      tickWidth: 2,
      minorTickColor: '#933',
      tickLength: 8,
      minorTickLength: 4,
      labels: {
          distance: -28,
          rotation: 'auto',
          color:'#202020',
           style: {  fontSize: '16px' }
      },
      endOnTick: false,
      plotBands: [{
          from: 0,
          to: 15,
          color: '#EE101F',
          innerRadius: '96%',
          outerRadius: '105%'
      }]
      
  }, {
      pane: 2,
      min: 1,
      max: 3,
      categories: ["","Run","Wait","Inactive"],
      //tickInterval: 1,
      tickWidth: 2,
      tickPosition: 'outside',
      lineColor: '#000000',
      tickColor: '#202020',
      lineWidth: 2,
      minorTickPosition: 'outside',
      minorTickColor: '#933',
      tickLength: 10,
      minorTickLength: 0,
      labels: {
          distance: 24,
          rotation: 0,
          style: {  fontSize: '14px' , fontWeight: 'bold' }
      },
      offset: -85,
      endOnTick: false
  }],

  series: [
  {
    yAxis: 0,
      name: 'Speed',
      data: [0],
       dial: {
        backgroundColor: '#C02316',
              radius: '90%',
              baseWidth: 4,
              baseLength: '90%',
              rearLength: 0
              
          },
      dataLabels: {
          format: '<span style="color:#339">{y} m/s</span><br/>' ,
             enabled:true,
                x: -70,
                y: -10,
      },
      tooltip: {
          valueSuffix: ' m/s'
      }
  },
      {
        yAxis: 1,
      name: 'Battery',
      data: [0],
       dial: {
        backgroundColor: '#C02316',
              radius: '90%',
              baseWidth: 4,
              baseLength: '90%',
              rearLength: 0
              
          },
      dataLabels: {
             x: 80,
             y: -10,
          format:
              '<span style="color:#933">{y} %</span>',
      },
      tooltip: {
          valueSuffix: ' %'
      }
  },
          {
        yAxis: 2,
      name: 'Status',
      data: [3],
      dial: {
        backgroundColor: '#202020',
              radius: '40%',
              baseWidth: 10,
              baseLength: '90%',
              rearLength: 10
          },
              pivot: {
                radius: 10,
                backgroundColor: '#202020'
              },
      dataLabels: {
      enabled:false,
             x: -70,
             y: -50,
          format:
              '<span style="color:#933">{y} stat</span>'
      },
      tooltip: {
          valueSuffix: ' Status'
      }
  }]
};

} 
