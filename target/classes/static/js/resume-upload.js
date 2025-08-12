/**
 * Integrated resume upload and ATS analysis in a single flow
 * This handles both uploading the resume file and getting its ATS score
 */
async function handleResumeUpload(file, appwriteUserId) {
    console.log('Handling resume upload and analysis for user:', appwriteUserId);
    
    if (!file) {
        console.warn('No file provided for upload');
        return null;
    }
    
    try {
        // Show loading indicator if available
        const loadingIndicator = document.getElementById('uploadStatusIndicator');
        if (loadingIndicator) {
            loadingIndicator.style.display = 'block';
        }
        
        // Create form data for file upload
        const formData = new FormData();
        formData.append('file', file);
        
        // Upload the file - this includes ATS analysis in the backend
        const response = await fetch('/api/v1/upload/resume', {
            method: 'POST',
            headers: {
                'X-Appwrite-User-Id': appwriteUserId
            },
            body: formData
        });
        
        if (!response.ok) {
            throw new Error(`Upload failed with status: ${response.status}`);
        }
        
        const result = await response.json();
        console.log('Resume upload and analysis successful:', result);
        
        if (result.success) {
            // Hide loading indicator if available
            if (loadingIndicator) {
                loadingIndicator.style.display = 'none';
            }
            
            // Return the data including the ATS score
            return result.data;
        } else {
            throw new Error(result.message || 'Upload failed');
        }
        
    } catch (error) {
        console.error('Resume upload and analysis error:', error);
        
        // Hide loading indicator if available
        const loadingIndicator = document.getElementById('uploadStatusIndicator');
        if (loadingIndicator) {
            loadingIndicator.style.display = 'none';
        }
        
        return null;
    }
}
