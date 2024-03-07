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

  // my add
  $(document.querySelector('.fade-transition').classList.add('fade-transition-active'));

  // $(document).ready(function() {
  //   // Next button click event
  //   $('#openingButton').click(function(e) {
  //       e.preventDefault();
        
  //       // Hide the first card content and show the second
  //       $('#openingCard1').addClass('d-none');
  //       $('#openingCard2').removeClass('d-none');
  //       $('#nextButton').removeClass('d-none');
  //   });
  // });

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

  $(document).ready(function() {
    $('.toggle-sidebar').click(function() {
        $('#collapseTwo').addClass('show');
    });
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
