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
  
  var userPName = ["HPP 물질제조", "PP 압출", "부산-청주 운송", "매립", "폐플라스틱재활용"];
  var lciDb = ['호모폴리프로필렌-HPP', '컴파운딩PP압출', '공로수송', '혼합플라스틱매립', '펠렛용폐플라스틱재활용'];
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

  const waterBar = {
    labels: userPName,
    datasets: [
      {
        label: 'Water',
        data: [1053.38148, 0.4334, 0.084689, 0, 0.008246],
        backgroundColor: '#7A9D54', 
        stack: 'Stack 0',
      },
    ]
  };

  const resourceBar = {
    labels: userPName,
    datasets: [
      {
        label: 'Sulfur',
        data: [4.4627004E-6, 9.476926072012841E-7, 1.5960000529240003E-7, 4.011347681986201E-8, 4.011347681986201E-9],
        backgroundColor: '#7A9D54', 
        stack: 'Stack 0',
      },
      {
        label: 'Uranium',
        data: [4.093278E-6, 3.07919852E-11,  1.5960000000000003E-7,  1.5960000000000003E-7, 5.2924E-15],
        backgroundColor: '#884ab2',
        stack: 'Stack 0',
      },
      {
        label: 'Lead',
        data: [7.4467738E-9, 0, 7.4467738E-15, 0, 0],
        backgroundColor: '#BC131F',
        stack: 'Stack 0',
      },
    ]
  };
  
  const humanBar = {
    labels: userPName,
    datasets: [
      {
        label: 'Sulfur',
        data: [3.9480048, 0.69490, 0.19552, 0.19552, 0.01319],
        backgroundColor: '#7A9D54', 
        stack: 'Stack 0',
      },
      {
        label: 'Uranium',
        data: [ 0.42105006, 0,  0.0919,  0, 0],
        backgroundColor: '#884ab2',
        stack: 'Stack 0',
      },
      {
        label: 'Lead',
        data: [0, 0.00642, 0, 0, 0.12226],
        backgroundColor: '#BC131F',
        stack: 'Stack 0',
      },
    ]
  };

  const eutrohBar = {
    labels: userPName,
    datasets: [
      {
        label: 'Nitrogen oxides',
        data: [0.4277, 0.07528, 0.02118, 0.19552, 0.00143],
        backgroundColor: '#7A9D54', 
        stack: 'Stack 0',
      },
      {
        label: 'Dinitrogen monoxide',
        data: [ 0.13413, 2.0052279E-4,  0,  0, 3.8070000000000006E-6],
        backgroundColor: '#884ab2',
        stack: 'Stack 0',
      },
      {
        label: 'COD, Chemical Oxygen Demand',
        data: [0.002293, 0, 4.5198285154E-5, 0, 0],
        backgroundColor: '#BC131F',
        stack: 'Stack 0',
      },
    ]


  };
const config = {
    type: 'bar',
    data: eutrohBar,
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

const waterPie = {
  // labels: ['폴리비닐클로라이드-PVC', 'PVC사출성형', '공로수송', '혼합플라스틱매립', '펠렛용폐플라스틱재활용'],
  labels: labelList,
  datasets: [
    {
      label: '물질별 환경영향값 비교',
      data: [1053.381487, 0.433408, 0.084689, 0.000388513, 0.0082464],
      backgroundColor: ['#164B60', '#1B6B93', '#4FC0D0', '#A1C2F1', '#A2FF86'],
    }
  ]
};

const HumanPie = {
  // labels: ['폴리비닐클로라이드-PVC', 'PVC사출성형', '공로수송', '혼합플라스틱매립', '펠렛용폐플라스틱재활용'],
  labels: labelList,
  datasets: [
    {
      label: '물질별 환경영향값 비교',
      data: [5.422504, 0.7106, 0.382615, 0.582615, 0.13564],
      backgroundColor: ['#164B60', '#1B6B93', '#4FC0D0', '#A1C2F1', '#A2FF86'],
    }
  ]
};
const resourcePie = {
  // labels: ['폴리비닐클로라이드-PVC', 'PVC사출성형', '공로수송', '혼합플라스틱매립', '펠렛용폐플라스틱재활용'],
  labels: labelList,
  datasets: [
    {
      label: '물질별 환경영향값 비교',
      data: [8.56765E-6, 4.01186201E-9, 9.4012841E-7, 9.47692641E-7, 1.59603E-7],
      backgroundColor: ['#164B60', '#1B6B93', '#4FC0D0', '#A1C2F1', '#A2FF86'],
    }
  ]
};
const eutrohPie = {
  // labels: ['폴리비닐클로라이드-PVC', 'PVC사출성형', '공로수송', '혼합플라스틱매립', '펠렛용폐플라스틱재활용'],
  labels: labelList,
  datasets: [
    {
      label: '물질별 환경영향값 비교',
      data: [0.5645, 0.0758289622, 0.02128411, 0.001440428, 0.001440428],
      backgroundColor: ['#164B60', '#1B6B93', '#4FC0D0', '#A1C2F1', '#A2FF86'],
    }
  ]
};
const config2 = {
  type: 'pie',
  data: eutrohPie,
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