/**
 * Toggle experience fragment content display.
 */
document.querySelectorAll('.wcmio-siteapi-genericedit-container .experienceFragment').forEach(xf => {
  xf.querySelector('input[type=checkbox]').addEventListener('click', () => {
    xf.classList.toggle('showContent');
  });
})
