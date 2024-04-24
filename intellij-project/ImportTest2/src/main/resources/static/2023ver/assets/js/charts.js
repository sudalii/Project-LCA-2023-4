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
  
  const data = {
    labels: ['A', 'B', 'C', 'D', 'E', 'F', 'G'],
    datasets: [
      {
        label: 'Dataset 1',
        data: [0.5, 0.3, 0.1, 0.4, 0.3, 0.6, 0.2],
        backgroundColor: '#7A9D54', 
        stack: 'Stack 0',
      },
      {
        label: 'Dataset 2',
        data: [0.1, 0.4, 0.5, 0.3, 0.4, 0.1, 0.7],
        backgroundColor: '#557A46',
        stack: 'Stack 0',
      },
      {
        label: 'Dataset 3',
        data: [0.1, 0.2, 0.1, 0.3, 0.2, 0.2, 0.1],
        backgroundColor: '#F2EE9D',
        stack: 'Stack 1',
      },
    ]
  };

const config = {
    type: 'bar',
    data: data,
    options: {
      plugins: {
        title: {
          display: true,
          text: 'Chart.js Bar Chart - Stacked'
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
const data2 = {
  labels: ['A', 'B', 'C', 'D', 'E'],
  datasets: [
    {
      label: 'Dataset 1',
      data: [79.2, 5.4, 10.9, 2.5, 2],
      backgroundColor: ['#164B60', '#1B6B93', '#4FC0D0', '#A1C2F1', '#A2FF86'],
    }
  ]
};

const config2 = {
  type: 'pie',
  data: data2,
  options: {
    responsive: true,
    plugins: {
      legend: {
        position: 'top',
      },
      title: {
        display: true,
        text: 'Chart.js Pie Chart'
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