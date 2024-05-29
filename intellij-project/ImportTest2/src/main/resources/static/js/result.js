(function($) {
    "use strict"; // Start of use strict
  
    // Retrieve and parse the hidden input value to JSON
    let resultsData = document.getElementById('resultsData').value;
    let results = JSON.parse(resultsData);
    const categories = /*[[${categories}]]*/ [];
        
    $(document).ready(function() {
        $('#categorySelect').on('change', function() {
            const selectedCategory = $(this).val();
            updateContent(selectedCategory);
        });
    });

    function updateContent(categoryName) {
        const category = categories.find(cat => cat.name === categoryName);
        if (category) {
            $('#method').text('➜ 계산에 사용된 LCIA 방법론: ' + category.method);

            let contentHtml = '<h4 class="h5 text-gray-800 text-center">' + category.name + ' 결과</h4>';

            contentHtml += '<div class="table-responsive" data-aos="fade-up">';
            contentHtml += '<table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">';
            contentHtml += '<thead><tr><th>물질명</th><th>LCI 분석 결과</th><th>특성화 계수</th><th>영향 평가 결과</th></tr></thead>';
            contentHtml += '<tbody>';

            const categoryResult = results.find(result => result.name === categoryName);
            if (categoryResult) {
                categoryResult.processResults.forEach(process => {
                    process.flowResults.forEach(flow => {
                        contentHtml += '<tr>';
                        contentHtml += '<td>' + flow.name + '</td>';
                        contentHtml += '<td>' + flow.lciResult + '</td>';
                        contentHtml += '<td>' + flow.cf + '</td>';
                        contentHtml += '<td>' + flow.impactResult + '</td>';
                        contentHtml += '</tr>';
                    });
                });
            }

            contentHtml += '</tbody></table></div>';
            
            $('#resultContent').html(contentHtml);
        }
    }

})(jQuery); // End of use strict
