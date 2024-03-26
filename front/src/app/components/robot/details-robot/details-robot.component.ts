import { Component, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts/highstock';

import dataInit  from 'highcharts/modules/data';
import seriesLabelInit  from  'highcharts/modules/series-label';
import  exportingInit  from "highcharts/modules/exporting";
import  exportDataInit  from "highcharts/modules/export-data";
import  accessibilityInit  from "highcharts/modules/accessibility";
import { RobotService } from 'src/app/core/services/robot.service';

dataInit(Highcharts);
seriesLabelInit(Highcharts);
exportingInit(Highcharts);
exportDataInit (Highcharts);
accessibilityInit(Highcharts);
@Component({
  selector: 'app-details-robot',
  templateUrl: './details-robot.component.html',
  styleUrls: ['./details-robot.component.css']
})
export class DetailsRobotComponent implements OnInit {

constructor(public robotService: RobotService){

}
  ngOnInit(): void {
    Highcharts.stockChart('container-stock-chart-robot',this.chartOptions);
  }
  onClickRefresh():void{}



  
  chartOptions : any =   {
    // chart: {
    //     type: 'line',
    //     scrollablePlotArea: {
    //       minWidth: 700
    //     }
    //   },
    title: {
        text: 'Robot AGV Statistics',
        align: 'left'
    },

    subtitle: {
        text: 'Statistics about battery and speed',
        align: 'left'
    }, 
    rangeSelector: {
        selected: 1
    },
    yAxis: [{
        title: {enabled: true,text: 'Speed'}
        , labels: { format: '{text}mm/s', align: 'right', x: -3 },
        gridLineWidth: 0,    crosshair: true, // lineColor: '#FF0000',lineWidth: 1,
        top: '0%',height: '75%', //,  height: '50%', 
        //alternateGridColor: '#FDFFD5',
        plotLines: [{
            value: this.robotService.settingRobot.speed.max,
            color: 'red',
            dashStyle: 'shortDash',
            width: 2,
            zIndex: 1000,
            label: {
                align: 'left',
                text: 'max speed',
                x: 10
            }
        },
        {
            value: this.robotService.settingRobot.speed.min,
            color: 'red',
            dashStyle: 'shortDash',
            width: 2,
            zIndex: 1000,
            label: {
                text: 'min speed',
                align: 'left',
                x: 10
            }
        }
    ],  
    }
    , { // right y axis
       // linkedTo: 1,
        title: { enabled: true,  text: "Battery Level" },
        gridLineWidth: 0,   crosshair: true,
        opposite: false,
        labels: {format: '{value:.,0f}%', align: 'right', x: -3},
        top: '0%',height: '75%',//top: '50%',height: '50%',
        offset: 0,  
    },

    {
        title: {
            text: 'Status'
        },
        top: '75%',
        height: '25%',
        offset: 0,
        
        
         categories: ['WAITING', 'RUNNING', 'INACTIVE'],
         //reversed: true,
    }
],

    xAxis: {
        //crosshair: true,
        plotBands: [{
            from: 1647523800000,
            to: 1647869400000,
            color: '#FED9C9',
            zIndex: 2,
            label: {
                text: 'Disconnected',
                align: 'center',
                x: 0
            }
        },
        {
            from: 1647437400000,
            to: 1647523800000,
            color: '#FFEFD8',
            zIndex: 1,
            label: {
                text: 'Manual',
                align: 'center',
                x: 0,y:40
            }
        },
        // {
        //     from: 1647869400000,
        //     to: 1647955800000,
        //     color: '#8FE9C6',
        //     label: {
        //         text: 'connected',
        //         align: 'center',
        //         x: 0
        //     }
        // }
    ],
        plotLines: [{
            value: 1647523800000,
            color: '#4EA291',
            width: 3
        }, {
            value: 1647869400000,
            color: '#4EA291',
            width: 3
        }],
    },
    tooltip: {
      split: true
  },
    legend: {
        enabled: true,
        layout: 'horizontal',
        align: 'center',
        verticalAlign: 'top',
        x: 0,
        y: 0
    },

    // plotOptions: {
    //     series: {
    //         label: {
    //             connectorAllowed: false
    //         },
    //         pointStart: 2010
    //     }
    // },

    series: [{
        name: 'Speed',
        data: [[1647437400000,8.59],[1647523800000,7.62],[1647610200000,7.98],[1647869400000,7.38],[1647955800000,4.82],[1648042200000,4.21],[1648128600000,6.07],[1648215000000,1.72],[1648474200000,4.6],[1648560600000,7.96],[1648647000000,4.77],[1648733400000,7.61],[1648819800000,4.31],[1649079000000,7.44],[1649165400000,7.06],[1649251800000,7.83],[1649338200000,4.14],[1649424600000,4.09],[1649683800000,5.75],[1649770200000,4.66],[1649856600000,4.4],[1649943000000,5.29],[1650288600000,4.07],[1650375000000,4.4],[1650461400000,5.23],[1650547800000,5.42]]
   ,      tooltip: { valueDecimals: 2,  valueSuffix: 'm/s'}
    },
    {type: 'areaspline',
        name: 'Battery',
        data: [[1647437400000,90.59],[1647523800000,80.62],[1647610200000,50.98],[1647869400000,60.38],[1647955800000,70.82],[1648042200000,58.21],[1648128600000,25.07],[1648215000000,30.72],[1648474200000,14.6],[1648560600000,10.96],[1648647000000,7.77],[1648733400000,8.61],[1648819800000,60.31],[1649079000000,20.44],[1649165400000,30.06],[1649251800000,36.83],[1649338200000,78.14],[1649424600000,80.09],[1649683800000,99.75],[1649770200000,10.66],[1649856600000,2.4],[1649943000000,30.29],[1650288600000,80.07],[1650375000000,100.0],[1650461400000,70.23],[1650547800000,88.42]]
   ,      tooltip: { valueDecimals: 2,  valueSuffix: '%'}, yAxis: 1
    },
    {
        type: 'column',
              name: 'Status',
              data: [[1647437400000,1],[1647523800000,	2],[1647610200000,1],[1647869400000,0]],
              yAxis: 2,
                  tooltip: {
                    pointFormatter: function (this: Highcharts.Point) {
                        return '<span style="color:' + this.color + '">\u25CF</span> ' + this.series.name + ': <b>' + this.y + '</b><br/>' +
                            'Type: ' + (this.category === 'WAITING' ? 'WAITING' :this.category === 'RUNNING' ? 'RUNNING' : 'INACTIVE' );
                    }

      }
    }
],

    responsive: {
        rules: [{
            condition: {
                maxWidth: 500
            },
            chartOptions: {
                legend: {
                    layout: 'horizontal',
                    align: 'center',
                    verticalAlign: 'bottom'
                }
            }
        }]
    }
  };





}
