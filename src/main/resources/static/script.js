const URL = 'https://duotones-4cb36ca3dbe7.herokuapp.com/';
async function fetchImage() {
  try {
    const response = await fetch('https://duotones-4cb36ca3dbe7.herokuapp.com/api/images/coverImages');
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    const imageUrls = await response.json();
    imageData = imageUrls.map(item => ({
      url: item[0],
      title: item[1],
      folder_id: item[2],
      id : item[3]
    }));
    displayImages(imageData);
    
  } catch (error) {
    console.error('Error fetching images:', error);
  }
}

function displayImages(imageData) {
  const gallery = document.getElementById('imageContainer');
  gallery.innerHTML = ''; // Clear previous images
  gallery.style.display = 'flex';
  gallery.style.flexWrap = 'wrap'; // Allow wrapping to the next line
  gallery.style.justifyContent = 'space-between'; // Space out the images evenly

  imageData.forEach(item => {
    const imgContainer = document.createElement('div');

    imgContainer.style.flex = '0 0 48%'; // Each image container takes up 48% of the width
    imgContainer.style.marginBottom = '48px';
    imgContainer.style.overflow = 'hidden'; // Prevent image from exceeding container
    const anchor = document.createElement('a');
    
    anchor.id=item.id;
    anchor.className='workLink';
    anchor.href=anchor.href = `works.html?id=${item.id}`;
    const img = document.createElement('img');
    img.src = item.url;
    img.style.width = '100%'; // Image takes full width of its container
    img.style.height = '560px'; // Maintain aspect ratio
    img.style.objectFit = 'cover';
    img.style.transition = 'transform 0.3s'; // Smooth transition for zoom effect

    // Add hover effect for zooming in
    img.addEventListener('mouseover', () => {
      img.style.transform = 'scale(1.03)'; // Zoom in
    });
    img.addEventListener('mouseout', () => {
      img.style.transform = 'scale(1)'; // Reset zoom
    });

    const titleButton = document.createElement('button');
    titleButton.textContent = item.title;
    titleButton.style.fontSize = '14px';
    titleButton.style.marginTop = '8px';
    titleButton.style.border = 'none';
    titleButton.style.background = 'none';
    titleButton.style.cursor = 'pointer';
    titleButton.style.textAlign = 'left';
    titleButton.style.padding = '0';

    anchor.appendChild(img);
    imgContainer.appendChild(anchor);
    imgContainer.appendChild(titleButton);
    gallery.appendChild(imgContainer);
    
    //displayWorks(folder_id);
  });
}

fetchImage();


