// Retrieve and parse the hidden input value to JSON
let resultsData = document.getElementById('resultsData').value;
let results = JSON.parse(resultsData);

// 선택된 카테고리에 따라 필터링된 데이터를 사용하여 그래프를 렌더링하는 함수
function renderGraphs(category) {
    let filteredResults = results.find(result => result.name === category);

    if (!filteredResults) return;

    // 데이터 준비 (예시 데이터 구조)
    let pieData = {
        labels: filteredResults.processResults.map(p => p.name),
        datasets: [{
            data: filteredResults.processResults.map(p => p.value),
            backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF']
        }]
    };

    let barLabels = [];
    let barData = [];

    filteredResults.processResults.forEach(p => {
        p.flowResults.forEach(f => {
            barLabels.push(f.name);
            barData.push(f.impactResult);
        });
    });

    let barChartData = {
        labels: barLabels,
        datasets: [{
            label: '영향 평가 결과',
            data: barData,
            backgroundColor: '#36A2EB'
        }]
    };

    // Pie Chart
    let pieCtx = document.getElementById('pie').getContext('2d');
    new Chart(pieCtx, {
        type: 'doughnut',
        data: pieData,
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });

    // Bar Chart
    let barCtx = document.getElementById('stackedBarWithGroup').getContext('2d');
    new Chart(barCtx, {
        type: 'bar',
        data: barChartData,
        options: {
            responsive: true,
            maintainAspectRatio: false,
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
    renderGraphs('GWP');

    document.getElementById('categorySelect').addEventListener('change', function() {
        let selectedCategory = this.value;
        renderGraphs(selectedCategory);
    });
});
