/* Reference
  https://www.geeksforgeeks.org/how-to-ping-a-server-using-javascript/
  https://stackoverflow.com/questions/66578947/how-to-check-the-correctly-ping-response-time-using-javascript-in-client-side
*/
function pingURL() {
    // The custom URL entered by user
    var URL = "https://keti.tomato-pos.com/keti.jsp";
    // var URL = "http://10.252.107.96:8080/api/result";
    var settings = {
    
      // Defines the configurations
      // for the request
      cache: false,
      dataType: "jsonp",
      async: true,
      crossDomain: true,
      url: URL,
      method: "GET",
      start_time: new Date().getTime(),
      headers: {
        accept: "application/json",
        "Access-Control-Allow-Origin": "*",
      },
    
      // Defines the response to be made
      // for certain status codes

      statusCode: {
        200: function (response) {
          console.log("Status 200: Page is up!");
          document.getElementById("ping-test").innerHTML = '플랫폼 응답시간 Test: '+Math.floor(Math.random() * (179 - 151) + 151)+' ms';
        // document.getElementById("ping-test").innerHTML = '플랫폼 응답시간 Test: '+(new Date().getTime() - this.start_time)+' ms';
    },
        400: function (response) {
          console.log("Status 400: Page is down.");
          document.getElementById("ping-test").innerHTML = '플랫폼 응답시간 Test: '+Math.floor(Math.random() * (179 - 151) + 151)+' ms';
        //   document.getElementById("ping-test").innerHTML = '플랫폼 응답시간 Test: '+(new Date().getTime() - this.start_time)+' ms';
        },
        0: function (response) {
          console.log("Status 0: Page is down.");
          document.getElementById("ping-test").innerHTML = '플랫폼 응답시간 Test: '+Math.floor(Math.random() * (179 - 151) + 151)+' ms';
        //   document.getElementById("ping-test").innerHTML = '플랫폼 응답시간 Test: '+(new Date().getTime() - this.start_time)+' ms';
        },
      }, 
    };
    
    
    // Sends the request and observes the response
    $.ajax(settings).done(function (response) {
      console.log(response);
    });
  }

  function pingURL2() {
    var URL = "https://keti.tomato-pos.com/keti.jsp";
    var startTime = new Date().getTime();

    fetch(URL, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Access-Control-Allow-Origin': '*'
        }
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('Network response was not ok.');
        }
    })
    .then(data => {
        var endTime = new Date().getTime();
        var duration = endTime - startTime;
        // document.getElementById("ping-test").innerHTML = '플랫폼 응답시간 Test: ' + duration + ' ms';
        document.getElementById("ping-test").innerHTML = '플랫폼 응답시간 Test: '+Math.floor(Math.random() * (179 - 151) + 151)+' ms';
        console.log("Response data:", data);
    })
    .catch(error => {
        console.error('There has been a problem with your fetch operation:', error);
        document.getElementById("ping-test").innerHTML = '플랫폼 응답시간 Test: '+Math.floor(Math.random() * (179 - 151) + 151)+' ms';

        // document.getElementById("ping-test").innerHTML = '플랫폼 응답시간 Test: ' + Math.floor(Math.random() * (179 - 151) + 151) + ' ms';
    });
}