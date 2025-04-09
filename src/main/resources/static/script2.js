async function loadHeader() {
    try {
        const response = await fetch('header.html');
        if (!response.ok) {
            throw new Error('Header file not found');
        }
        const data = await response.text();
        document.getElementById('header-container').innerHTML = data;
    } catch (error) {
        console.error('Error loading header:', error);
    }
  }
async function loadcontact() {
    try {
        const response = await fetch('contact.html');
        if (!response.ok) {
            throw new Error('Header file not found');
        }
        const data = await response.text();
        document.getElementById('contact-container').innerHTML = data;
    } catch (error) {
        console.error('Error loading header:', error);
    }
  }
async function loadfooter() {
    try {
        const response = await fetch('footer.html');
        if (!response.ok) {
            throw new Error('footer file not found');
        }
        const data = await response.text();
        document.getElementById('footer-container').innerHTML = data;
    } catch (error) {
        console.error('Error loading header:', error);
    }
  }

  
async function getAllProjects(){
      try{
          const response = await fetch('https://duotones-4cb36ca3dbe7.herokuapp.com/api/images/all');
        
          if(!response.ok){
            throw new Error(`HTTP error! Status: ${response.status}`);
          }

          const data = await response.json();
          allData = data.map(item=> ({
            id: item.id,
            title: item.title,
            coverDesc: item.coverDescription,
            longDesc: item.longDescription,
            url: item.coverImage,
            folder_id: item.folderId

          }));
          console.log(allData);
          displayWorks(allData);
      }
      catch(e){
          console.error(e);
      }
    }

    function displayWorks(imageData) {
        const gallery = document.getElementById('imageContainer4');
        gallery.innerHTML = ''; // Clear previous content
        gallery.style.display = 'flex';
        gallery.style.flexDirection = 'column'; // Stack items vertically
        gallery.style.gap = '80px'; // Spacing between sections
    
        imageData.forEach(item => {
            const projectContainer = document.createElement('div');
            projectContainer.style.display = 'flex';
            projectContainer.style.flexDirection = 'column';
            projectContainer.style.gap = '10px'; // Space between title & image
    
            // **Title (Project Name)**
            const projectTitle = document.createElement('h3');
            projectTitle.textContent = item.title.toUpperCase(); // Convert to uppercase for style
            projectTitle.style.fontSize = '14px';
            projectTitle.style.fontWeight = 'bold';
            projectTitle.style.margin = '0';
            projectTitle.style.letterSpacing = '1px';
    
            // **Image Container**
            const imgContainer = document.createElement('div');
            imgContainer.style.width = '100%';
            imgContainer.style.overflow = 'hidden';
    
            const anchor = document.createElement('a');
            anchor.href = `works.html?id=${item.id}`;
            anchor.className = 'workLink';
    
            const img = document.createElement('img');
            img.src = item.url;
            img.style.width = '100%';
            img.style.height = '600px';
            img.style.objectFit = 'cover';
            img.style.transition = 'transform 0.3s ease';
    
            // Hover effect
            img.addEventListener('mouseover', () => {
                img.style.transform = 'scale(1.02)';
            });
            img.addEventListener('mouseout', () => {
                img.style.transform = 'scale(1)';
            });
    
            // Append elements in order
            anchor.appendChild(img);
            imgContainer.appendChild(anchor);
            projectContainer.appendChild(projectTitle);
            projectContainer.appendChild(imgContainer);
            gallery.appendChild(projectContainer);
        });
    }
    
    getAllProjects();

  loadHeader();
  loadcontact();
  loadfooter();