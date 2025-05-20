
document.addEventListener('DOMContentLoaded', function() {
    // Function to check if element is in viewport
    function isInViewport(element) {
      const rect = element.getBoundingClientRect();
      return (
        rect.top <= (window.innerHeight || document.documentElement.clientHeight) &&
        rect.bottom >= 0
      );
    }
  
    // Function to handle scroll animations
    function handleScrollAnimations() {
      const animatedElements = document.querySelectorAll('.animated');
      
      animatedElements.forEach(element => {
        if (isInViewport(element)) {
          element.classList.add('visible');
        }
      });
    }
  
    // Initial check for elements in viewport
    handleScrollAnimations();
  
    // Add scroll event listener
    window.addEventListener('scroll', handleScrollAnimations);
    
    // Add smooth scrolling for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
      anchor.addEventListener('click', function (e) {
        e.preventDefault();
        
        document.querySelector(this.getAttribute('href')).scrollIntoView({
          behavior: 'smooth'
        });
      });
    });
  });
  
  // Add some subtle parallax effect to the hero background
  window.addEventListener('scroll', function() {
    const scrollPosition = window.scrollY;
    const heroContainer = document.querySelector('.hero-container');
    
    if (heroContainer) {
      heroContainer.style.backgroundPositionY = scrollPosition * 0.3 + 'px';
    }
  });