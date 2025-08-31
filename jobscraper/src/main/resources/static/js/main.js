const jobForm = document.getElementById('jobForm');
    const loading = document.getElementById('loading');
    if(jobForm){
    jobForm.addEventListener('submit', (e) => {
        loading.style.display = 'block';
        Swal.fire({
            title: 'Fetching jobs...',
            text: 'Please wait while we fetch the latest jobs for you.',
            timerProgressBar: true,
            didOpen: () => Swal.showLoading(),
            allowOutsideClick: false
        });
    });
}
    // Profile form storage
    const cardsContainer = document.getElementById('cardsContainer');
     if(cardsContainer){
        displayProfile();
     }

    const profileForm = document.getElementById('profileForm');
    if(profileForm){
    profileForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const profileData = {
            fullName: document.getElementById('fullName').value,
            github: document.getElementById('github').value,
            linkedin: document.getElementById('linkedin').value,
            skills:document.getElementById('skills').value,
            files: {}
        };

        const fileInputs = ['resume','photo','idCard','certifications'];

        const readFilesPromises = fileInputs.map(id => {
            const input = document.getElementById(id);
            const files = input.files;
            if(files.length > 0){
                return Promise.all(Array.from(files).map(file => new Promise((resolve, reject) => {
                    const reader = new FileReader();
                    reader.onload = () => resolve({name:file.name,data:reader.result});
                    reader.onerror = err => reject(err);
                    reader.readAsDataURL(file);
                }))).then(data => { profileData.files[id] = data; });
            } else { profileData.files[id] = []; return Promise.resolve(); }
        });

        Promise.all(readFilesPromises).then(() => {
            localStorage.setItem('userProfile', JSON.stringify(profileData));
            Swal.fire('Profile Saved!', 'Your files and info are stored locally.', 'success');
            displayProfile();
        }).catch(err => console.error(err));
    });
}
    // Display stored profile
    function displayProfile(){
        const savedProfile = JSON.parse(localStorage.getItem('userProfile'));
        if(!savedProfile) return;

        cardsContainer.innerHTML = '';

        ['fullName','skills','github','linkedin'].forEach(key => {
            if(savedProfile[key]){
                const card = document.createElement('div');
                card.className='col-md-4';
                card.innerHTML=`
                    <div class="card card-shadow p-3 h-100">
                        <h5>${key.charAt(0).toUpperCase()+key.slice(1)}</h5>
                        <p>${savedProfile[key]}</p>
                    </div>`;
                cardsContainer.appendChild(card);
            }
        });

        Object.keys(savedProfile.files).forEach(type=>{
            savedProfile.files[type].forEach(file=>{
                const card = document.createElement('div');
                card.className='col-md-4';
                card.innerHTML=`
                    <div class="card card-shadow p-3 h-100">
                        <h5>${type}</h5>
                        <p>${file.name}</p>
                        <a href="${file.data}" download="${file.name}" class="btn btn-sm btn-success">Download</a>
                    </div>`;
                cardsContainer.appendChild(card);
            });
        });
    }

    window.addEventListener('load', displayProfile);