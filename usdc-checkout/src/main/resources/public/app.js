async function generateQR() {
  const amount = document.getElementById("amount").value;
  if (!amount || amount <= 0) {
    alert("Please enter a valid USDC amount.");
    return;
  }

  try {
    const response = await fetch(`/generate-qr/${amount}`);
    const html = await response.text();
    document.getElementById("qrResult").innerHTML = html;
  } catch (error) {
    console.error("Error generating QR:", error);
    alert("Failed to generate QR. Please try again.");
  }
}

