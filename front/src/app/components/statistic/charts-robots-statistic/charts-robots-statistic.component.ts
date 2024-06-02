import { Component, OnInit } from '@angular/core';

import * as Highcharts from 'highcharts';
//import dataInit from 'highcharts/modules/data';
//import seriesLabelInit from 'highcharts/modules/series-label';
import exportingInit from "highcharts/modules/exporting";
import exportDataInit from "highcharts/modules/export-data";
import accessibilityInit from "highcharts/modules/accessibility";
//import noDataToDisplayInit from 'highcharts/modules/no-data-to-display';
//import annotationsInit from 'highcharts/modules/annotations';
//dataInit(Highcharts);
//seriesLabelInit(Highcharts);
exportingInit(Highcharts);
exportDataInit(Highcharts);
accessibilityInit(Highcharts);
//noDataToDisplayInit(Highcharts);
//annotationsInit(Highcharts);
@Component({
  selector: 'app-charts-robots-statistic',
  templateUrl: './charts-robots-statistic.component.html',
  styleUrls: ['./charts-robots-statistic.component.css']
})
export class ChartsRobotsStatisticComponent  implements OnInit {
  ngOnInit(): void {
    Highcharts.chart('container', this.chartOptions);

  }
  onClickStopAllRobot():void{
  }
  onClickStartAllRobot():void{
  }
  onClickTrunOffAllRobot():void{
  }












  chartOptions: any = {
    chart: {
      type: 'bar'
  },
  title: {
      text: 'Historic World Population by Region',
      align: 'left'
  },
  subtitle: {
      text: 'Source: <a ' +
          'href="https://en.wikipedia.org/wiki/List_of_continents_and_continental_subregions_by_population"' +
          'target="_blank">Wikipedia.org</a>',
      align: 'left'
  },
  xAxis: {
      categories: ['Robot-1', 'Robot-2', 'Robot-3', 'Robot-4','Robot-5','Robot-6','Robot-7','Robot-8','Robot-9','Robot-10'],
      title: {
          text: null
      },
      gridLineWidth: 1,
      lineWidth: 0
  },
  yAxis: {
      min: 0,
      title: {
          text: 'Population (millions)',
          align: 'high'
      },
      labels: {
          overflow: 'justify'
      },
      gridLineWidth: 0
  },
  tooltip: {
      valueSuffix: ' millions'
  },
  plotOptions: {
      bar: {
          borderRadius: '50%',
          dataLabels: {
              enabled: true
          },
          groupPadding: 0.1
      }
  },
  legend: {
      layout: 'vertical',
      align: 'right',
      verticalAlign: 'top',
      x: -40,
      y: 80,
      floating: true,
      borderWidth: 1,
      backgroundColor:
          '#FFFFFF',
      shadow: true
  },
  credits: {
      enabled: false
  },
  series: [{
      name: 'Year 1990',
      data: [631, 727, 3202, 721,721,721,721,721,721,721]
  }, {
      name: 'Year 2000',
      data: [814, 841, 3714, 726,726,726,726,726,726,726]
  }, {
      name: 'Year 2018',
      data: [1276, 1007, 4561, 746,746,746,746,746,746,1]
  }]
  };













}