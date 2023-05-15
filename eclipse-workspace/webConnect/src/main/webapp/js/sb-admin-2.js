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


/* https://codepen.io/alexandracaulea/pen/YzXdNbY */
/* MIT license */

  function Survey(survey) {
    if (!survey) {
      throw new Error("No Survey Form found!");
    }
  
    // select the elements
    const progressbar = survey.querySelector(".progressbar");
    const surveyPanels = survey.querySelectorAll(".survey__panel");
    const question1Radios = survey.querySelectorAll("[name='question_1']");
    const question2Radios = survey.querySelectorAll("[name='question_2']");
    const question3CheckBoxes = survey.querySelectorAll("[name='question_3']");
    const question4Radios = survey.querySelectorAll("[name='question_4']");
    const question4Textarea = survey.querySelector("[name='recommendText']");
    const question5Name = survey.querySelector("[name='name']");
    const question5Email = survey.querySelector("[name='email']");
    const question5Country = survey.querySelector("[name='country']");
    const question5Age = document.querySelector("[name='age']");
    const allPanels = Array.from(survey.querySelectorAll(".survey__panel"));
    let progressbarStep = Array.from(
      progressbar.querySelectorAll(".progressbar__step ")
    );
    const mainElement = document.querySelector("main");
    const nextButton = survey.querySelector("[name='next']");
    const prevButton = survey.querySelector("[name='prev']");
    const submitButton = survey.querySelector("[name='submit']");
    let currentPanel = Array.from(surveyPanels).filter((panel) =>
      panel.classList.contains("survey__panel--current")
    )[0];
    const formData = {};
    const options = {
      question1Radios,
      question2Radios,
      question3CheckBoxes,
      question4Radios,
      question4Textarea,
      question5Name,
      question5Email,
      question5Country,
      question5Age
    };
    let dontSubmit = false;
  
    function storeInitialData() {
      allPanels.map((panel) => {
        let index = panel.dataset.index;
        let panelName = panel.dataset.panel;
        let question = panel
          .querySelector(".survey__panel__question")
          .textContent.trim();
        formData[index] = {
          panelName: panelName,
          question: question
        };
      });
    }
  
    function updateProgressbar() {
      let index = currentPanel.dataset.index;
      let currentQuestion = formData[`${parseFloat(index)}`].question;
      progressbar.setAttribute("aria-valuenow", index);
      progressbar.setAttribute("aria-valuetext", currentQuestion);
      progressbarStep[index - 1].classList.add("active");
    }
  
    function updateFormData({ target }) {
      const index = +currentPanel.dataset.index;
      const { name, type, value } = target;
      if (type === "checkbox") {
        if (formData[index].answer === undefined) {
          formData[index].answer = {
            [name]: [value]
          };
          return;
        }
        if (formData[index]["answer"][`${name}`].includes(value)) {
          const position = formData[index]["answer"][`${name}`].findIndex(
            (elem) => elem === value
          );
          formData[index]["answer"][`${name}`].splice(position, 1);
        } else {
          formData[index]["answer"][`${name}`].push(value);
        }
        return;
      }
      if (index === 4 || index === 5) {
        let copy;
        const original = formData[index].answer;
        if (original === undefined) {
          formData[index].answer = {
            [name]: value
          };
          copy = { ...formData[index].answer };
        } else {
          formData[index].answer = { ...original, [name]: value };
        }
        return;
      }
  
      formData[index].answer = {
        [name]: value
      };
    }
  
    function showError(input, text) {
      const formControl = input.parentElement;
      const errorElement = formControl.querySelector(".error-message");
      errorElement.innerText = text;
      errorElement.setAttribute("role", "alert");
      if (survey.classList.contains("form-error")) return;
      survey.classList.add("form-error");
    }
  
    function noErrors(input) {
      if (!input) {
        const errorElement = currentPanel.querySelector(".error-message");
        errorElement.textContent = "";
        errorElement.removeAttribute("role");
        survey.classList.remove("form-error");
        return;
      }
      const formControl = input.parentElement;
      const errorElement = formControl.querySelector(".error-message");
      errorElement.innerText = "";
      errorElement.removeAttribute("role");
    }
  
    function getName(input) {
      if (input.name === "age") return "Age";
      if (input.name === "country") return "Country";
      return `${input.id.charAt(0).toUpperCase()}${input.id.slice(1)}`;
    }
  
    function checkEmail(input) {
      if (input.value.trim() === "") {
        showError(input, `${getName(input)} is required`);
      } else {
        const pattern = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if (pattern.test(input.value.trim())) {
          noErrors(input);
        } else {
          showError(input, "Email is not valid");
        }
      }
    }
  
    function checkRequired(input) {
      if (input.value.trim() === "") {
        showError(input, `${getName(input)} is required`);
      } else {
        noErrors(input);
      }
    }
  
    function checkSelection(input) {
      if (input.selectedIndex === 0) {
        showError(input, `${getName(input)} is required`);
      } else {
        noErrors(input);
      }
    }
  
    function checkAge(age) {
      if (age.value === "") {
        showError(age, `${getName(age)} is required`);
        return;
      }
      if (+age.value > 0) {
        noErrors(age);
      }
    }
  
    function checkRequirements() {
      const requirement = currentPanel.dataset.requirement;
      const index = currentPanel.dataset.index;
      const errorElement = currentPanel.querySelector(".error-message");
  
      if (!formData[index].hasOwnProperty("answer") && +index === 5) {
        checkRequired(question5Name);
        checkEmail(question5Email);
        checkSelection(question5Country);
        checkAge(question5Age);
      } else if (formData[index].hasOwnProperty("answer") && +index === 5) {
        const req = requirement.split(";");
        let data = Object.keys(formData[index].answer);
        let arr = [];
        let res;
        for (let i = 0; i < data.length; i++) {
          res = req.includes(data[i]) ? data[i] : "";
          arr.push(res);
        }
        if (arr.includes("name")) checkRequired(question5Name);
        if (arr.includes("email")) checkEmail(question5Email);
        if (arr.includes("country")) checkSelection(question5Country);
        if (arr.includes("age")) checkAge(question5Age);
        if (
          arr.length === 4 &&
          arr.every((elem) => formData[index].answer.hasOwnProperty(elem))
        ) {
          survey.classList.remove("form-error");
          dontSubmit = true;
        }
      } else {
        errorElement.textContent = `Select an ${requirement} to continue.`;
        errorElement.setAttribute("role", "alert");
        survey.classList.add("form-error");
      }
    }
  
    function updateProgressbarBar() {
      const index = currentPanel.dataset.index;
      let currentQuestion = formData[`${parseFloat(index)}`].question;
      progressbar.setAttribute("aria-valuenow", index);
      progressbar.setAttribute("aria-valuetext", currentQuestion);
      progressbarStep[index].classList.remove("active");
    }
  
    function displayNextPanel() {
      currentPanel.classList.remove("survey__panel--current");
      currentPanel.setAttribute("aria-hidden", true);
      currentPanel = currentPanel.nextElementSibling;
      currentPanel.classList.add("survey__panel--current");
      currentPanel.setAttribute("aria-hidden", false);
      updateProgressbar();
      if (+currentPanel.dataset.index > 1) {
        prevButton.disabled = false;
        prevButton.setAttribute("aria-hidden", false);
      }
      if (+currentPanel.dataset.index === 5) {
        nextButton.disabled = true;
        nextButton.setAttribute("aria-hidden", true);
        submitButton.disabled = false;
        submitButton.setAttribute("aria-hidden", false);
      }
    }
  
    function displayPrevPanel() {
      currentPanel.classList.remove("survey__panel--current");
      currentPanel.setAttribute("aria-hidden", true);
      currentPanel = currentPanel.previousElementSibling;
      currentPanel.classList.add("survey__panel--current");
      currentPanel.setAttribute("aria-hidden", false);
      updateProgressbarBar();
      if (+currentPanel.dataset.index === 1) {
        prevButton.disabled = true;
        prevButton.setAttribute("aria-hidden", true);
      }
      if (+currentPanel.dataset.index < 5) {
        nextButton.disabled = false;
        nextButton.setAttribute("aria-hidden", false);
        submitButton.disabled = true;
        submitButton.setAttribute("aria-hidden", true);
      }
    }
  
    function handleprevButton() {
      displayPrevPanel();
    }
  
    function handleNextButton() {
      const index = currentPanel.dataset.index;
      if (!formData[index].hasOwnProperty("answer")) {
        checkRequirements();
      } else {
        noErrors();
        displayNextPanel();
      }
    }
  
    // submitting the form
    function handleFormSubmit(e) {
      checkRequirements();
      if (!dontSubmit) {
        e.preventDefault();
      } else {
        mainElement.classList.add("submission");
        mainElement.setAttribute("role", "alert");
        mainElement.innerHTML = `<svg width="126" height="118" fill="none" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 126 118" aria-hidden="true" style="transform: translateX(50%)"><path d="M52.5 118c28.995 0 52.5-23.729 52.5-53S81.495 12 52.5 12 0 35.729 0 65s23.505 53 52.5 53z" fill="#B9CCED"/><path d="M45.726 87L23 56.877l8.186-6.105 15.647 20.74L118.766 0 126 7.192 45.726 87z" fill="#A7E9AF"/></svg>
        <h2 class="submission">Thanks for your time</h2>
        <p>The form was successfully submitted`;
        return false;
      }
    }
  
    storeInitialData();
  
    // Add event listeners
    function addListenersTo({
      question1Radios,
      question2Radios,
      question3CheckBoxes,
      question4Radios,
      ...inputs
    }) {
      question1Radios.forEach((elem) =>
        elem.addEventListener("change", updateFormData)
      );
      question2Radios.forEach((elem) =>
        elem.addEventListener("change", updateFormData)
      );
      question3CheckBoxes.forEach((elem) =>
        elem.addEventListener("change", updateFormData)
      );
      question4Radios.forEach((elem) =>
        elem.addEventListener("change", updateFormData)
      );
      let {
        question4Textarea,
        question5Name,
        question5Email,
        question5Country,
        question5Age
      } = inputs;
      question4Textarea.addEventListener("change", updateFormData);
      question5Name.addEventListener("change", updateFormData);
      question5Email.addEventListener("change", updateFormData);
      question5Country.addEventListener("change", updateFormData);
      question5Age.addEventListener("change", updateFormData);
    }
    nextButton.addEventListener("click", handleNextButton);
    prevButton.addEventListener("click", handleprevButton);
    addListenersTo(options);
    survey.addEventListener("submit", handleFormSubmit);
  }
  
  const survey = Survey(document.querySelector(".survey"));
  

/*   // script for tab steps
  $('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {

    var href = $(e.target).attr('href');
    var $curr = $(".process-model  a[href='" + href + "']").parent();

    $('.process-model li').removeClass();

    $curr.addClass("active");
    $curr.prevAll().addClass("visited");
  });
  // end  script for tab steps */

})(jQuery); // End of use strict




