const searchInput = document.querySelector("#search-p1");
const dropdown = document.querySelector(".dropdown");
const dropdownMenu = document.querySelector(".dropdown-menu");
let active = -1;

const initialOptions = [
  "Initial option #1",
  "Initial option #2",
  "Initial option #3"
];

const options = [
  "New South Wales",
  "Queensland",
  "Victoria",
  "South Australia",
  "Western Australia",
  "Northern Territory",
  "Tasmania",
  "ACT"
];

function populateOptions(options) {
  dropdownMenu.innerHTML = "";
  options
    .map((opt) => `<a class="dropdown-item" href="#">${opt}</a>`)
    .forEach((html) => dropdownMenu.insertAdjacentHTML("beforeend", html));
  if ( options.length == 0 ) {
    dropdownMenu.insertAdjacentHTML("beforeend", `<a class="dropdown-item disabled">Couldn't find nuffin</a>`  );
  }
}

searchInput.addEventListener("focusin", (e) => {
  console.log("focus in");
  e.target.readOnly = false;
  dropdown.classList.add("focus");
  dropdownMenu.classList.add("show");

  populateOptions(initialOptions);
});

searchInput.addEventListener("focusout", (e) => {
  console.log("focus out");
  dropdown.classList.remove("focus");
  dropdownMenu.classList.remove("show");
  e.target.value = "";
  e.target.readOnly = true;
  active = -1;
});

searchInput.addEventListener("input", (e) => {
  console.log("input", e.target.value);
  const len = e.target.value.length;
  if (len == 0) {
    populateOptions(initialOptions);
  } else {
    populateOptions(options.filter((opt) => opt.includes(e.target.value)));
  }
});

searchInput.addEventListener("keydown", (e) => {
  console.log("keydown", e.key);

  // remove focus on 'Escape'
  if (e.key === "Escape") {
    document.querySelector("#search-p1").blur();
  }
  
  // direction arrow navigation
  const dropdownItems = dropdownMenu.querySelectorAll(".dropdown-item");
  if ( e.key === "ArrowDown" && active < dropdownItems.length-1 ) {
    active++;
  }
  if ( e.key === "ArrowUp" && active > 0 ) {
    active--;
  }
  if ( active >= 0 ) {
    dropdownItems.forEach( e => e.classList.remove("active"));
    dropdownItems[active].classList.add( "active" );
  }
});

document.addEventListener("keydown", (e) => {
  console.log("doc keydown", e.key);

  // focus on '/' key
  if (e.key === "/" && e.target.tagName !== "INPUT") {
    e.preventDefault();
    document.querySelector("#search-p1").focus();
  }
});
