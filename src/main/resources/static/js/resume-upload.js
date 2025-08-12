/**
 * Integrated resume upload and ATS analysis in a single flow
 * This handles both uploading the resume file and getting its ATS score
 */
async function handleResumeUpload(file, appwriteUserId) {
    console.log('=== RESUME UPLOAD START ===');
    console.log('Handling resume upload and analysis for user:', appwriteUserId);
    console.log('File details:', {
        name: file.name,
        size: file.size,
        type: file.type
    });
    
    if (!file) {
        console.warn('No file provided for upload');
        return null;
    }
    
    // Validate file size (5MB limit)
    if (file.size > 5 * 1024 * 1024) {
        console.error('File too large:', file.size);
        showMessage('File size exceeds 5MB limit', 'error');
        return null;
    }
    
    // Validate file type
    const allowedTypes = ['application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'];
    if (!allowedTypes.includes(file.type)) {
        console.error('Invalid file type:', file.type);
        showMessage('Please upload a PDF, DOC, or DOCX file', 'error');
        return null;
    }
    
    try {
        // Show loading indicator
        showMessage('Uploading resume and analyzing...', 'info');
        
        // Create form data for file upload
        const formData = new FormData();
        formData.append('file', file);
        
        console.log('Sending upload request to /api/v1/upload/resume');
        console.log('Headers:', { 'X-Appwrite-User-Id': appwriteUserId });
        
        // Upload the file - this includes ATS analysis in the backend
        const response = await fetch('/api/v1/upload/resume', {
            method: 'POST',
            headers: {
                'X-Appwrite-User-Id': appwriteUserId
            },
            body: formData
        });
        
        console.log('Upload response status:', response.status);
        console.log('Upload response headers:', response.headers);
        
        if (!response.ok) {
            const errorText = await response.text();
            console.error('Upload failed with response:', errorText);
            throw new Error(`Upload failed with status: ${response.status} - ${errorText}`);
        }
        
        const result = await response.json();
        console.log('Resume upload and analysis successful:', result);
        
        if (result.success) {
            showMessage('Resume uploaded and analyzed successfully!', 'success');
            console.log('=== RESUME UPLOAD SUCCESS ===');
            console.log('File URL:', result.data?.fileUrl);
            console.log('ATS Score:', result.data?.atsScore);
            
            // Return the data including the ATS score
            return result.data;
        } else {
            throw new Error(result.message || 'Upload failed');
        }
        
    } catch (error) {
        console.error('=== RESUME UPLOAD ERROR ===');
        console.error('Resume upload and analysis error:', error);
        showMessage(`Resume upload failed: ${error.message}`, 'error');
        return null;
    }
}

/**
 * Show messages to user
 */
function showMessage(message, type = 'info') {
    console.log(`[${type.toUpperCase()}] ${message}`);
    
    // Use our dedicated message container
    const messageContainer = document.getElementById('messageContainer');
    const messageText = document.getElementById('messageText');
    
    if (messageContainer && messageText) {
        messageText.textContent = message;
        messageContainer.className = `message-container ${type}`;
        messageContainer.style.display = 'block';
        
        // Auto-hide success and info messages after 5 seconds
        if (type === 'success' || type === 'info') {
            setTimeout(() => {
                messageContainer.style.display = 'none';
            }, 5000);
        }
    } else {
        // Fallback to console and alert
        console.log('Message container not found, using fallback');
        if (type === 'error') {
            alert('Error: ' + message);
        } else if (type === 'success') {
            alert('Success: ' + message);
        }
    }
}
