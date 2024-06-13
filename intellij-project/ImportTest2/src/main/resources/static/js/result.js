(function($) {
    "use strict"; // Start of use strict

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

    $(document).ready(function() {
        $('#categorySelect').on('change', function() {
            const selectedCategory = $(this).val();
            updateContent(selectedCategory);
        });
    });

    function updateContent(categoryName) {
        const category = categories.find(cat => cat.name === categoryName);
        if (!category) {
            console.error("Category not found: " + categoryName);
            $('#resultContent').html('<p>No data available for the selected category.</p>');
            return;
        }

        $('#method').text('➜ 계산에 사용된 LCIA 방법론: ' + category.method);

        let contentHtml = '<h4 class="h5 text-gray-800 text-center">' + category.name + ' 물질 분석 표</h4>';
        contentHtml += '<div class="table-responsive" data-aos="fade-up">';
        contentHtml += '<table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">';
        contentHtml += '<thead><tr><th>물질명</th><th>특성화 계수</th><th>영향 평가 결과</th></tr></thead>';
        contentHtml += '<tbody>';

        category.flows.forEach(flow => {
            contentHtml += '<tr>';
            contentHtml += '<td>' + flow.name + '</td>';
            contentHtml += '<td>' + flow.cf + ' kg' + '</td>';
            contentHtml += '<td>' + flow.impactResult + ' ' + category.result.unit + '</td>';
            contentHtml += '</tr>';
        });

        contentHtml += '</tbody></table></div>';
        $('#resultContent').html(contentHtml);

        const impactValueHtml = '➜ 총 환경영향값: ' + category.result.value + ' ' + category.result.unit;
        $('#impactValue').text(impactValueHtml);
    }

})(jQuery); // End of use strict