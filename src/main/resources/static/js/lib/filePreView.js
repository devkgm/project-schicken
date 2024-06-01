

async function filePreView(url, fileName, fileExtention){
    const modal = new bootstrap.Modal(document.getElementById("filePreView-modal"));
    if(fileExtention === "pdf"){
        window.open("/pdf/web/viewer.html?file="+url);
    } else {
        const reader = new FileReader();
        const result =await fetch(url);
        const blob = await result.blob()
        const file = new File([blob],fileName+"."+fileExtention,{type:blob.type})
        reader.onload = function(event) {
            const img = document.getElementById("filePreView-img");
            img.src = reader.result
            modal.show();
        };
        reader.readAsDataURL(file);
    }
}
