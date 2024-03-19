import { Component, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts/highstock';

import dataInit  from 'highcharts/modules/data';
import seriesLabelInit  from  'highcharts/modules/series-label';
import  exportingInit  from "highcharts/modules/exporting";
import  exportDataInit  from "highcharts/modules/export-data";
import  accessibilityInit  from "highcharts/modules/accessibility";

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


  ngOnInit(): void {














    
    Highcharts.stockChart('container-stock-chart-robot',this.chartOptions);
  }
  onClickRefresh():void{

  }



  
  chartOptions : any =   {
    // chart: {
    //     type: 'line',
    //     scrollablePlotArea: {
    //       minWidth: 700
    //     }
    //   },
    title: {
        text: 'U.S Solar Employment Growth',
        align: 'left'
    },

    subtitle: {
        text: 'By Job Category. Source: <a href="https://irecusa.org/programs/solar-jobs-census/" target="_blank">IREC</a>.',
        align: 'left'
    }, 
    rangeSelector: {
        selected: 1
    },
    yAxis: [{
        title: {enabled: true,text: 'Speed'}
        , labels: { format: '{text}mm/s', align: 'right', x: -3 },
        gridLineWidth: 0,    crosshair: true,  lineColor: '#FF0000',lineWidth: 1,
        height: '100%', //,  height: '50%', 
        //alternateGridColor: '#FDFFD5',
        
        plotLines: [{
            value: 6000,
            color: 'red',
            dashStyle: 'shortDash',
            width: 2,
            label: {
                text: 'Last quarter maximum'
            }
        },
        {
            value: 5000,
            color: 'red',
            dashStyle: 'shortDash',
            width: 2,
            label: {
                text: 'Last quarter maximum',
                align: 'right',
                x: -3
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
        top: '0%',height: '100%',//top: '50%',height: '50%',
        offset: 0,  
    }
],

    xAxis: {
        //crosshair: true,
        plotBands: [{
            from: 1647523800000,
            to: 1647869400000,
            color: '#F59098',
            label: {
                text: 'Disconnected',
                align: 'center',
                x: 0
            }
        },
        {
            from: 1647869400000,
            to: 1647955800000,
            color: '#8FE9C6',
            label: {
                text: 'connected',
                align: 'center',
                x: 0
            }
        }
    
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
        data: [[1647437400000,8000.59],[1647523800000,7589.62],[1647610200000,5875.98],[1647869400000,1258.38],[1647955800000,4527.82],[1648042200000,4528.21],[1648128600000,6028.07],[1648215000000,174.72],[1648474200000,4542.6],[1648560600000,7875.96],[1648647000000,4524.77],[1648733400000,7585.61],[1648819800000,4524.31],[1649079000000,7875.44],[1649165400000,7587.06],[1649251800000,7857.83],[1649338200000,4254.14],[1649424600000,4527.09],[1649683800000,5757.75],[1649770200000,4542.66],[1649856600000,4524.4],[1649943000000,5758.29],[1650288600000,4542.07],[1650375000000,4527.4],[1650461400000,5787.23],[1650547800000,5857.42]]
   ,      tooltip: { valueDecimals: 2,  valueSuffix: 'mm/s'}
    },
    {
        name: 'Battery',
        data: [[1647437400000,90.59],[1647523800000,80.62],[1647610200000,50.98],[1647869400000,60.38],[1647955800000,70.82],[1648042200000,58.21],[1648128600000,25.07],[1648215000000,30.72],[1648474200000,14.6],[1648560600000,10.96],[1648647000000,7.77],[1648733400000,8.61],[1648819800000,60.31],[1649079000000,20.44],[1649165400000,30.06],[1649251800000,36.83],[1649338200000,78.14],[1649424600000,80.09],[1649683800000,99.75],[1649770200000,10.66],[1649856600000,2.4],[1649943000000,30.29],[1650288600000,80.07],[1650375000000,100.0],[1650461400000,70.23],[1650547800000,88.42]]
   ,      tooltip: { valueDecimals: 2,  valueSuffix: '%'}, yAxis: 1
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
