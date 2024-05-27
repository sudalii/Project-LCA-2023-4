// import Utils from './utils.js';
// import 'chartjs-adapter-luxon';

// import * as Utils from "./utils.js";
// import { Chart } from 'https://cdn.jsdelivr.net/npm/chart.js';
// import { Chart as Chart$1 } from 'https://cdn.jsdelivr.net/npm/chart.js'; // This is used to register plugins

// Chart.register(Chart$1);

/**
 * Stacked Bar Chart With Group
 */
  const DATA_COUNT = 7;
  const NUMBER_CFG = {count: DATA_COUNT, min: -100, max: 100};
  
  var userPName = ["PVC 물질제조", "PVC 사출성형", "부산-청주 운송", "매립", "폐플라스틱재활용"];
  var lciDb = ['폴리비닐클로라이드-PVC', 'PVC사출성형', '공로수송', '혼합플라스틱매립', '펠렛용폐플라스틱재활용'];
  var labelList = [];
  for (let i=0; i<userPName.length; i++) {
     var temp = userPName[i] + "\n" + "(사용된 DB: " + lciDb[i] + ")";
    labelList.push(temp); 
  }

  const GWPBar = {
    labels: userPName,
    datasets: [
      {
        label: 'CO2',
        data: [0, 0, 0.01219, 140.50489, 0],
        backgroundColor: '#7A9D54', 
        stack: 'Stack 0',
      },
      {
        label: 'Methane',
        data: [1148.58870, 202.83620, 0.00031, 0.00018, 0.02282],
        backgroundColor: '#884ab2',
        stack: 'Stack 0',
      },
      {
        label: 'Dinitrogen monoxide',
        data: [4125.89100, 8.31865, 0.000009, 0.0002, 0.00093],
        backgroundColor: '#BC131F',
        stack: 'Stack 0',
      },
    ]
  };

const config = {
    type: 'bar',
    data: GWPBar,
    options: {
      plugins: {
        title: {
          display: true,
          text: '물질별 환경영향값 비교',
          font: {
            size: 25
          }
        },
      },
      responsive: true,
      interaction: {
        intersect: false,
      },
      scales: {
        x: {
          stacked: true,
        },
        y: {

          stacked: true
        }
      }
    }
  };

// Input HTML tag
const stackedBarWithGroup = new Chart(
  document.getElementById('stackedBarWithGroup'),
  config
);


/**
 * Pie
 */


console.log(labelList, labelList.type);

const GWPPie = {
  // labels: ['폴리비닐클로라이드-PVC', 'PVC사출성형', '공로수송', '혼합플라스틱매립', '펠렛용폐플라스틱재활용'],
  labels: labelList,
  datasets: [
    {
      label: '물질별 환경영향값 비교',
      data: [5275.777118, 211.1551936, 0.0125112, 0.000388513, 0.023754142],
      backgroundColor: ['#164B60', '#1B6B93', '#4FC0D0', '#A1C2F1', '#A2FF86'],
    }
  ]
};
const HumanPie = {
  labels: ['폴리비닐클로라이드-PVC', 'PVC사출성형', '공로수송', '혼합플라스틱매립', '펠렛용폐플라스틱재활용'],
  datasets: [
    {
      label: 'Dataset 1',
      data: [5275.777118, 211.1551936, 0.0125112, 0.000388513, 0.023754142],
      backgroundColor: ['#164B60', '#1B6B93', '#4FC0D0', '#A1C2F1', '#A2FF86'],
    }
  ]
};

const config2 = {
  type: 'pie',
  data: GWPPie,
  options: {
    responsive: true,
    plugins: {
      legend: {
        position: 'top',
        text: 'kg CO2 eq'
      },
      title: {
        display: true,
        text: '공정별 환경영향값 비교',
        font: {
          size: 25
        }
      }
    }
  },
};

// Input HTML tag
const pie = new Chart(
  document.getElementById('pie'),
  config2
);

/**
 * Line Segment Styling
 */
const skipped = (ctx, value) => ctx.p0.skip || ctx.p1.skip ? value : undefined;
const down = (ctx, value) => ctx.p0.parsed.y > ctx.p1.parsed.y ? value : undefined;

const genericOptions = {
  fill: false,
  interaction: {
    intersect: false
  },
  radius: 0,
};

const config3 = {
  type: 'line',
  data: {
    labels: ['A', 'B', 'C', 'D', 'E', 'F', 'G'],
    datasets: [{
      label: 'Dataset',
      data: [65, 59, NaN, 48, 56, 57, 40],
      borderColor: 'rgb(75, 192, 192)',
      segment: {
        borderColor: ctx => skipped(ctx, 'rgb(0,0,0,0.2)') || down(ctx, 'rgb(192,75,75)'),
        borderDash: ctx => skipped(ctx, [6, 6]),
      },
      spanGaps: true
    }]
  },
  options: genericOptions
};

// Input HTML tag
const line = new Chart(
  document.getElementById('line'),
  config3
);