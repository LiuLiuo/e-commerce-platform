function Banner() {
    this.bannerwidth =700;
    this.bannerGroup = $("#banner-group");
    this.index = 1;
    this.bannerUl = $("#banner-ul");
    this.liList = this.bannerUl.children("li");
    this.bannerCount = this.liList.length;
    this.pageControl =$(".page-control");
}

Banner.prototype.initBanner=function(){
    var self =this;
    var firstBanner = self.liList.eq(0).clone();
    var lastBanner = self.liList.eq(self.bannerCount-1).clone();
    self.bannerUl.append(firstBanner);
    self.bannerUl.prepend(lastBanner);
    this.bannerUl.css({"width":self.bannerwidth*(self.bannerCount+2),"left":-self.bannerwidth});
};

Banner.prototype.initPageControl = function(){
    var self=this;
        for(var i = 0; i<self.bannerCount;i++){
        var circle = $("<li></li>");
        self.pageControl.append(circle);
        if (i === 0){
            circle.addClass("active");
        }
    }
    self.pageControl.css({"width":self.bannerCount*12+8*2+16*(self.bannerCount-1)});
};

Banner.prototype.listenBannerHover = function(){
    var self =this;
    this.bannerGroup.hover(function () {
        //第一个函数,你把鼠标移动到banner上会执行的函数
        clearInterval(self.timer);
        self.toggleArrow(true);
    },function () {
        self.loop();
        self.toggleArrow(false);
    });
};

Banner.prototype.animate = function(){
    var self =this;
    self.bannerUl.animate({"left":-700*self.index},800);
    var index = self.index;
    if(index === 0){
        index = self.bannerCount-1;

    }else if(index === self.bannerCount+1){
        index = 0;
    }else{
        index= self.index-1;
    }
        self.pageControl.children('li').eq(index).addClass("active").siblings().removeClass("active");
};

Banner.prototype.loop = function () {
    var self = this;
    // bannerUI.css({"left":-800});
    //定时器
    this.timer = setInterval(function(){
        if(self.index >= self.bannerCount+1){
            self.bannerUl.css({"left":-self.bannerwidth});
            self.index = 2;
        }else{
            self.index++;
        }
        self.animate();
    },2000);
};


Banner.prototype.listenPageControl = function(){
    var self = this;
    self.pageControl.children("li").each(function(index,obj){
        $(obj).click(function(){
           self.index =index;
           self.animate();
        });
    });
};

Banner.prototype.run= function(){
    console.log("running...");
    this.initBanner();
    this.initPageControl();
    this.loop();
    // this.listenArrowClick();
    this.listenPageControl();
    this.listenBannerHover();

};
//初始化
$(function () {
    var banner = new Banner();
    banner.run();

});