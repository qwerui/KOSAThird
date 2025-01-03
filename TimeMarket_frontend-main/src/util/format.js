export default {
    formatDate(str){
        const date = new Date(str);

        const hour = date.getHours() < 10 ? '0'+date.getHours() : date.getHours();
        const minute = date.getMinutes() < 10 ? '0'+date.getMinutes() : date.getMinutes();
        return (date.getMonth()+1)+"/"+date.getDate()+" "+hour+":"+minute;

    }
}