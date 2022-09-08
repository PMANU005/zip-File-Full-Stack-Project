import React , {useState} from 'react';

function FileUploadPage(){

    const [selectedFile, setSelectedFile] = useState();
	const [isSelected, setIsSelected] = useState(false);
	const [selectedFileSize, setSelectedFileSize] = useState();

	const changeHandler = (event) => {
		console.log(event.target)
          setSelectedFile(event.target.files[0]);
          setSelectedFileSize(selectedFile.size)
		  setIsSelected(true);
	}
    
	const handleSubmission =  (event) => {
			const fileData = new FormData();
			fileData.append('Incoming UnZip File', selectedFile);
			fetch(
				
				'http://localhost:8080/zipFile/', 
				{
					method: 'POST',
					body: fileData,
				},
				{
					responseType: 'blob'
				}
			)
			.then((response) => response.blob())
			 .then((blob) => {
				const url = window.URL.createObjectURL(new Blob([blob]));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', 'file.zip');
                document.body.appendChild(link);
                link.click();
                link.parentNode.removeChild(link);
				 
			})
			.then((result) => {
				console.log('Success:', result);
			})
			.catch((error) => {
					console.error('Error:', error);
			});
	};

	return(
   <div>
			<input type="file" name="file" onChange={changeHandler} />
			{isSelected  ? (
				<div>
					<p>Filename: {selectedFile.name}</p>
					<p>Filetype: {selectedFile.type}</p>
					<p>Size in bytes: {selectedFile.size}</p>
					<p>
						lastModifiedDate:{' '}
						{selectedFile.lastModifiedDate.toLocaleDateString()}
					</p>

                    <button onClick={handleSubmission}>Submit</button>

				</div>
			) : (
				<p>Please select a file to submit</p>
			)}
		</div>
	)
}

export default FileUploadPage;