(function($) {
  "use strict"; // Start of use strict

  // Toggle the side navigation
  $("#sidebarToggle, #sidebarToggleTop").on('click', function(e) {
    $("body").toggleClass("sidebar-toggled");
    $(".sidebar").toggleClass("toggled");
    if ($(".sidebar").hasClass("toggled")) {
      $('.sidebar .collapse').collapse('hide');
    };
  });

  // Close any open menu accordions when window is resized below 768px
  $(window).resize(function() {
    if ($(window).width() < 768) {
      $('.sidebar .collapse').collapse('hide');
    };
    
    // Toggle the side navigation when window is resized below 480px
    if ($(window).width() < 480 && !$(".sidebar").hasClass("toggled")) {
      $("body").addClass("sidebar-toggled");
      $(".sidebar").addClass("toggled");
      $('.sidebar .collapse').collapse('hide');
    };
  });

  // Prevent the content wrapper from scrolling when the fixed side navigation hovered over
  $('body.fixed-nav .sidebar').on('mousewheel DOMMouseScroll wheel', function(e) {
    if ($(window).width() > 768) {
      var e0 = e.originalEvent,
        delta = e0.wheelDelta || -e0.detail;
      this.scrollTop += (delta < 0 ? 1 : -1) * 30;
      e.preventDefault();
    }
  });

  // Scroll to top button appear
  $(document).on('scroll', function() {
    var scrollDistance = $(this).scrollTop();
    if (scrollDistance > 100) {
      $('.scroll-to-top').fadeIn();
    } else {
      $('.scroll-to-top').fadeOut();
    }
  });

  // Smooth scrolling using jQuery easing
  $(document).on('click', 'a.scroll-to-top', function(e) {
    var $anchor = $(this);
    $('html, body').stop().animate({
      scrollTop: ($($anchor.attr('href')).offset().top)
    }, 1000, 'easeInOutExpo');
    e.preventDefault();
  });

  /*********************************************************************/
  /******************************* My add ******************************/
  /*********************************************************************/

  $(document).ready(function() {
    var fadeTransitionElement = document.querySelector('.fade-transition');
    if (fadeTransitionElement) {
        fadeTransitionElement.classList.add('fade-transition-active');
    }
  });
  
  $(document).ready(function() {
    // Next button click event
    // $('#openingButton').click(function(e) {
    $(document).on('keyup', '.form-group', function(e) {
        if (e.key == 'Enter' && $(this).val().trim() !== '') {
            e.preventDefault();
        }
        
        // Hide the first card content and show the second
        $('#openingCard2').removeClass('d-none');
    });
  });

  /* GET list */ 
  function loadProcessList() {
    var userId = $('#userId').val();
    var typeStr = $('#typeStr').val();
    $.ajax({
        url: `/services/${userId}/${typeStr}/list`,
        method: 'GET',
        success: function(data) {
          var contentHtml = '';
          var nameList = {
              'p2': '물질&부품제조',
              'p3': '가공',
              'p4': '수송',
              'p5': '재활용 및 폐기'
          };

          $.each(data, function(type, processes) {
              contentHtml += `<h6 class="collapse-header custom-color">${nameList[type]}</h6>`;
              $.each(processes, function(index, p) {
                  contentHtml += `<a class="collapse-item" href="/services/${userId}/${type}/${p.id}">${p.processName}</a>`;
              });
          });
          $('#processListContent').html(contentHtml);
      },
      error: function() {
          alert('리스트를 불러오는데 실패했습니다.');
      }
    });
  }

  function loadProductList() {
    $.ajax({
        url: `/services/list`,
        method: 'GET',
        success: function(data) {
          var contentHtml = '';

          $.each(data, function(index, products) {
            contentHtml += `<a class="collapse-item" href="/services/${products.id}/p1">${products.productName}</a>`;
          });
          $('#productListContent').html(contentHtml);
      },
      error: function() {
          alert('리스트를 불러오는데 실패했습니다.');
      }
    });
  }

  $(document).ready(function() {
    $('.toggle-sidebar').click(function() {
        $('#collapseTwo').toggleClass('show');
       
        // 버튼 텍스트 변경
        if ($('#collapseTwo').hasClass('show')) {
          $('.toggle-text').text(' 목록 접기');
          loadProcessList();
      } else {
          $('.toggle-text').text(' 목록 보기');
      }

    });
  });  

  $(document).ready(function() {
    $('#showProcessList').click(function() { 
      loadProcessList();
    });
  });

  $(document).ready(function() {
    $('#showProductList').click(function() { 
      loadProductList();
    });
  });

  /*********************** p1-p5 유효성 검증 로직 ***********************/
  $(document).ready(function() {
    $('.form-action').click(function(event) {
        event.preventDefault(); // 기본 동작 중지
        var typeStr = $('#typeStr').val(); // typeStr 값을 가져옴
        var formId = "#" + typeStr + "Form";

        // typeStr이 'p4'일 경우 특별 계산 실행
        if (typeStr === 'p4' && !setProcessAmount()) {
            alert('거리 또는 무게를 입력해주세요.');
            return; // 계산 값이 유효하지 않으면 반환
        }

        // 폼 유효성 검사
        if (!validateForm(formId)) {
          return; // 유효하지 않으면 반환
        } 

        // 삭제 버튼 특별 처리
        if (this.id === 'updateDeleteButton') {
            $('#hiddenInput').attr('name', '_method').attr('value', 'delete');
        }

        $(formId).submit(); // 폼 제출
    });
});

// 폼의 모든 필드가 채워졌는지 확인하고, 누락된 필드를 사용자에게 알림
function validateForm(formId) {
  // 필드 이름과 사용자 친화적 이름의 매핑 객체
  var fieldNamesMap = {
    "processName": "공정 이름",
    "processAmount": "물질 및 부품 무게",
    "iFlow1": "전기 사용량",
    "iFlow2": "물 사용량",
    "oFlow1": "물 배출량"
  };

  var missingFields = []; // 누락된 필드 이름을 저장할 배열
  var formInput = formId + " input";
  $(formInput).each(function() {
      if (!$(this).val()) {
          var fieldName = $(this).attr('name'); // input 필드의 'name' 속성을 가져옵니다.
          // 매핑 객체에서 친화적 이름을 가져옵니다. 매핑이 없으면 필드 이름을 그대로 사용합니다.
          var friendlyName = fieldNamesMap[fieldName] || fieldName;
          missingFields.push(friendlyName);
      }
  });

  if (missingFields.length > 0) {
      alert("다음 필드를 채워주세요: " + missingFields.join(', ')); // 배열의 모든 요소를 쉼표로 구분하여 하나의 문자열로 합칩니다.
      return false; // 폼 제출을 막습니다.
  }

  return true; // 모든 필드가 채워져 있으면 폼 제출을 허용
}

// typeStr 'p4'일 때의 계산 로직
  // processAmount = 이동거리(iFlow1) * 차량무게(iFlow2)
  // unit이 DB에 저장될 때는 "무게*거리"로 들어가야 내부 계산이 쉬움 
  // processAmountUnit = iFlow2Unit*iFlow1Unit
  function setProcessAmount() {
    var iFlow1 = parseFloat($('#iFlow1').val());
    var iFlow2 = parseFloat($('#iFlow2').val());
    var iFlow1Unit = $('#iFlow1Unit').val();
    var iFlow2Unit = $('#iFlow2Unit').val();
    var processAmount = iFlow1 * iFlow2;
    var processAmountUnit = iFlow2Unit + "*" + iFlow1Unit;

    if (!isNaN(processAmount)) {
        $('#processAmount').val(processAmount);
        $('#processAmountUnit').val(processAmountUnit);
        return true; // 계산 성공
    } else {
        return false; // 계산 실패
    }
  }
  /*********************************************************************/

  /* Map Script - p4, 수송에서만 사용 */
  $(document).ready(function() {
    var baseLayer = L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors',
        maxZoom: 18
    });
    
    // heatmap config see https://www.patrick-wied.at/static/heatmapjs/example-heatmap-leaflet.html
    var cfg = {
        // radius should be small ONLY if scaleRadius is true (or small radius is intended)
        // if scaleRadius is false it will be the constant radius used in pixels
        radius: 5,
        maxOpacity: 0.8,
        minOpacity: 0.2,
        // scales the radius based on map zoom
        scaleRadius: true,
        // if set to false the heatmap uses the global maximum for colorization
        // if activated: uses the data maximum within the current map boundaries
        //   (there will always be a red spot with useLocalExtremas true)
        // "useLocalExtrema": true,
        // which field name in your data represents the latitude - default "lat"
        latField: 'latitude',
        // which field name in your data represents the longitude - default "lng"
        lngField: 'longitude',
        // which field name in your data represents the data value - default "value"
        valueField: 'weight',
        gradient: {
            // enter n keys between 0 and 1 here
            // for gradient color customization
            '.01': 'blue',
            '.95': 'red'
        }
    };

    var heatmapLayer = new HeatmapOverlay(cfg);

    var map = new L.Map('map', {
        center: new L.LatLng(36, 128),
        zoom: 7,
        layers: [baseLayer, heatmapLayer]
    });

    function setData(data) {
        if (!data) {
            return;
        }
        var max = 0.0;
        $.each(data, function(i, item) {
            if (item.weight > max) {
                max = item.weight;
            }
        });
        heatmapLayer.setData({
            max: max,
            data: data
        });
    }
  });


  /* https://codepen.io/blecaf/pen/YYwdJK */
  /* MIT lisence */
  /**This is just a demo to add the process classes**/
  $(document).ready(function($) {
    $(".process-step").click(function() {
      var theClass = $(this).attr("class").match(/(^|\s)step-\S+/g);
      var bute = $.trim(theClass);
      switch (bute) { 
        case "step-1": 
          $(".process-wrap").addClass("active-step1")
          break;
        case 'step-2': 
          $(".process-wrap").attr('class', 'process-wrap');
          $(this).parents(".process-wrap").addClass("active-step2")
          break;
        case 'step-3': 
          $(".process-wrap").attr('class', 'process-wrap');
          $(this).parents(".process-wrap").addClass("active-step3")
          break;
        case 'step-4': 
          $(".process-wrap").attr('class', 'process-wrap');
          $(this).parents(".process-wrap").addClass("active-step4")
          break;
        case 'step-5': 
          $(".process-wrap").attr('class', 'process-wrap');
          $(this).parents(".process-wrap").addClass("active-step5")
          break;
        case 'step-6': 
          $(".process-wrap").attr('class', 'process-wrap');
          $(this).parents(".process-wrap").addClass("active-step6")
          break; 		
        default:
          $(".process-wrap").attr('class', 'process-wrap');  
      }
    })
  });

})(jQuery); // End of use strict
