(async () => {
  // ANCHOR: body
  let resp = await fetch("http://localhost:8080", {
    method: "POST",
    body: JSON.stringify({
      jsonrpc: "2.0",
      id: 1,
      method: "result/total-impacts",
      params: {
        "@id": "86b36d3b-1dba-4804-bd61-dc5bcaf7c86c",
      }
    })
  });
  let v = await resp.json();
  console.log(v);
  // {
  //   jsonrpc: "2.0",
  //   result: [
  //     {
  //       impactCategory: {
  //         "@type": "ImpactCategory",
  //         "@id": "3bc1c67f-d3e3-3891-9fea-4512107d88ef",
  //         name: "Climate change",
  //         category: "EF 3.0 Method (adapted)",
  //         refUnit: "kg CO2 eq"
  //       },
  //       amount: 0.6387478227404646
  //     },
  // ...
  // ANCHOR_END: body
})();
