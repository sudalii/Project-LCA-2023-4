// Retrieve and parse the hidden input value to JSON
const GWPResult = JSON.parse(document.getElementById('GWPResult').value || '{}');
const GWPFlows = JSON.parse(document.getElementById('GWPFlows').value || '[]');
const WUResult = JSON.parse(document.getElementById('WUResult').value || '{}');
const WUFlows = JSON.parse(document.getElementById('WUFlows').value || '[]');
const ARDResult = JSON.parse(document.getElementById('ARDResult').value || '{}');
const ARDFlows = JSON.parse(document.getElementById('ARDFlows').value || '[]');
const HTResult = JSON.parse(document.getElementById('HTResult').value || '{}');
const HTFlows = JSON.parse(document.getElementById('HTFlows').value || '[]');
const EPResult = JSON.parse(document.getElementById('EPResult').value || '{}');
const EPFlows = JSON.parse(document.getElementById('EPFlows').value || '[]');

const categories = [
    { name: "지구온난화", result: GWPResult, flows: GWPFlows, method: "CML-IA baseline" },
    { name: "물 사용", result: WUResult, flows: WUFlows, method: "AWARE" },
    { name: "자원고갈", result: ARDResult, flows: ARDFlows, method: "CML-IA baseline" },
    { name: "인체독성", result: HTResult, flows: HTFlows, method: "CML-IA baseline" },
    { name: "부영양화", result: EPResult, flows: EPFlows, method: "CML-IA baseline" }
];


let pieChart;
let barChart;

function renderGraphs(categoryName) {
    const category = categories.find(cat => cat.name === categoryName);

    if (!category) return;

    // Pie Chart 데이터 준비
    let pieData = {
        labels: category.flows.map(f => f.name),
        datasets: [{
            data: category.flows.map(f => f.impactResult),
            backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF']
        }]
    };

    let barLabels = [];
    let barDataSets = [];

    category.flows.forEach((f, index) => {
        barLabels.push(f.name);
        barDataSets.push({
            label: f.name,
            data: [f.impactResult],
            backgroundColor: `#${Math.floor(Math.random()*16777215).toString(16)}`, // Random color for each bar
            stack: `Stack ${index}`  // 각 데이터셋마다 다른 스택 할당
        });
    });

    let barChartData = {
        labels: barLabels,
        datasets: barDataSets
    };

    // 기존 차트가 있다면 삭제
    if (pieChart) {
        pieChart.destroy();
    }

    if (barChart) {
        barChart.destroy();
    }

    // Pie Chart
    let pieCtx = document.getElementById('pie').getContext('2d');
    pieChart = new Chart(pieCtx, {
        type: 'pie',
        data: pieData,
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'top',
                    labels: {
                        generateLabels: function(chart) {
                            return chart.data.datasets[0].data.map((data, index) => ({
                                text: chart.data.labels[index] + ' (' + data + ' ' + category.result.unit + ')',
                                fillStyle: chart.data.datasets[0].backgroundColor[index]
                            }));
                        }
                    }
                },   
                title: {
                    display: true,
                    text: '공정별 환경영향값 비교',
                    font: {
                      size: 25
                    }
                }
            }
        }
    });

    // Bar Chart
    let barCtx = document.getElementById('stackedBarWithGroup').getContext('2d');
    barChart = new Chart(barCtx, {
        type: 'bar',
        data: barChartData,
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                    labels: {
                        generateLabels: function(chart) {
                            return chart.data.datasets.map((dataset, index) => ({
                                text: dataset.label + ' (' + dataset.data[0] + ' ' + category.result.unit + ')',
                                fillStyle: dataset.backgroundColor
                            }));
                        }
                    }
                },
                title: {
                    display: true,
                    text: '물질별 환경영향값 비교',
                    font: {
                        size: 25
                    }
                }
            },
            scales: {
                x: {
                    stacked: true
                },
                y: {
                    stacked: true
                }
            }
        }
    });
}

// 초기 그래프 렌더링 (기본 카테고리 선택)
document.addEventListener('DOMContentLoaded', function() {
    renderGraphs('지구온난화');

    document.getElementById('categorySelect').addEventListener('change', function() {
        let selectedCategory = this.value;
        renderGraphs(selectedCategory);
    });
});

