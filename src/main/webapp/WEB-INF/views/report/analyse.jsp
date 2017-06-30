<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.web.servlet.LocaleResolver"%>
<%@page import="org.apache.shiro.SecurityUtils"%>
<%@page import="com.asgab.service.account.ShiroDbRealm.ShiroUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
var gpHidden = ${gpHidden};
var currencyData = ${exchangeRates};
</script>
<div class="row">
	<div class="col-sm-6 col-md-4 col-lg-3" >
		<div class="form-group">
			<div id="reportDateDiv_2"
				style="background: #fff; cursor: pointer; padding: 7px 8px; border: 1px solid #ccc; width: 100%">
				<i class="pull-left glyphicon glyphicon-calendar fa fa-calendar"></i>&nbsp;
				<span id="reportDateSpan_2"></span> <b style="margin-top: 6px;"
					class="caret pull-right"></b> <input type="hidden"
					name="reportDate" id="reportDate_2">
			</div>
		</div>
	</div>
	<div class="col-sm-6 col-md-4 col-lg-3" >
		<div class="form-group">
			<select id="analyseCurrency" class="form-control select2" style="margin-right: 10px; width: 100%;">
				
			</select>
		</div>
	</div>
	<div class="col-sm-6 col-md-4 col-lg-3 col-sm-offset-6 col-lg-offset-3" >
	   <div class="form-group">
	       <a class="pull-right" id="downloadDashboard" style="cursor: pointer;margin: 10px 0 0 10px;"><i class="fa fa-fw fa-download"></i><spring:message code="report.download" /></a>
	   </div>
	</div>
</div>
<br/>
<div class="row">
	<div class="col-md-4">
		<div class="box box-widget widget-user">
			<!-- Add the bg color to the header using any of the bg-* classes -->
			<div class="widget-user-header bg-light-blue"
				style="height: 50px; padding: 10px;">
				<h3 class="widget-user-username" style="text-align: center;"><spring:message code="report.analyse.sale" /></h3>
			</div>

			<div class="box-footer" style="padding-top: 1px;">
				<div class="row">
					<div class="col-sm-6 border-right">
						<div class="description-block">
							<h5 class="description-header text-light-blue" id="totalOpportunity"></h5>
							<span class="description-text"><spring:message code="report.analyse.total.opportunity" /></span>
						</div>
						<!-- /.description-block -->
					</div>
					<!-- /.col -->
					<div class="col-sm-6 ">
						<div class="description-block">
							<h5 class="description-header text-light-blue" id="totalOrder"></h5>
							<span class="description-text"><spring:message code="report.analyse.total.order" /></span>
						</div>
						<!-- /.description-block -->
					</div>

				</div>
				<!-- /.row -->
			</div>
		</div>

	</div>

	<c:if test="${gpHidden eq false}">
	<div class="col-md-4">
		<div class="box box-widget widget-user">
			<!-- Add the bg color to the header using any of the bg-* classes -->
			<div class="widget-user-header bg-aqua"
				style="height: 50px; padding: 10px;">
				<h3 class="widget-user-username" style="text-align: center;"><spring:message code="report.analyse.gp" /></h3>
			</div>

			<div class="box-footer" style="padding-top: 1px;">
				<div class="row">
					<div class="col-sm-6 border-right">
						<div class="description-block">
							<h5 class="description-header text-light-blue" id="totalGPOpportunity"></h5>
							<span class="description-text"><spring:message code="report.analyse.total.opportunity" /></span>
						</div>
						<!-- /.description-block -->
					</div>
					<!-- /.col -->
					<div class="col-sm-6 ">
						<div class="description-block">
							<h5 class="description-header text-light-blue" id="totalGPOrder"></h5>
							<span class="description-text"><spring:message code="report.analyse.total.order" /></span>
						</div>
						<!-- /.description-block -->
					</div>

				</div>
				<!-- /.row -->
			</div>
		</div>
	</div>
	</c:if>


	<div class="col-md-4">
		<div class="box box-widget widget-user">
			<!-- Add the bg color to the header using any of the bg-* classes -->
			<div class="widget-user-header bg-teal "
				style="height: 50px; padding: 10px;">
				<h3 class="widget-user-username" style="text-align: center;"><spring:message code="report.analyse.total" /></h3>
			</div>

			<div class="box-footer" style="padding-top: 1px;">
				<div class="row">
					<div class="col-sm-4 border-right">
						<div class="description-block">
							<h5 class="description-header " id="advertiserCount"></h5>
							<span class="description-text"><spring:message code="report.analyse.advertiser" /></span>
						</div>
						<!-- /.description-block -->
					</div>
					<!-- /.col -->
					<div class="col-sm-4 border-right">
						<div class="description-block">
							<h5 class="description-header " id="orderCount"></h5>
							<span class="description-text"><spring:message code="report.analyse.total.order" /></span>
						</div>
						<!-- /.description-block -->
					</div>
					<!-- /.col -->
					<div class="col-sm-4">
						<div class="description-block">
							<h5 class="description-header " id="campaignCount"></h5>
							<span class="description-text"><spring:message code="report.analyse.campaigns" /></span>
						</div>
						<!-- /.description-block -->
					</div>
					<!-- /.col -->
				</div>
				<!-- /.row -->
			</div>
		</div>
	</div>


</div>

<div class="row">
	<div class="col-sm-12">
		<h5><spring:message code="report.analyse.sign.order" /></h5>
	</div>
</div>

<div class="row">

    <div class="col-md-12">

        <!-- BAR CHART -->
        <div class="box">
            <div class="box-header with-border">
                <h3 class="box-title"><spring:message code="report.sale.team" /></h3>
                <div class="box-tools pull-right">
                    <button class="btn btn-box-tool" data-widget="collapse">
                        <i class="fa fa-minus"></i>
                    </button>
                </div>
            </div>
            <div class="box-body">
                <div class="chart">
                    <canvas id="saleGroupChart" style="height: 460px; width: 934px;"></canvas>
                </div>
            </div>
            <!-- /.box-body -->
        </div>
        <!-- /.box -->

    </div>


    <div class="col-md-12">
        <div class="box">
            <div class="box-header with-border">
                <h3 class="box-title"><spring:message code="report.product" /></h3>
                <div class="box-tools pull-right">
                    <button class="btn btn-box-tool" data-widget="collapse">
                        <i class="fa fa-minus"></i>
                    </button>
                </div>
            </div>
            <div class="box-body">
                <div class="chart">
                    <canvas id="productChart" style="height: 230px; width: 934px;"></canvas>
                </div>
            </div>
            <!-- /.box-body -->
        </div>
        <!-- /.box -->


    </div>
    
</div>


<div class="row">
	
	<div class="col-md-6">

		<div class="box">
			<div class="box-header with-border">
				<h3 class="box-title"><spring:message code="report.analyse.income.manage" /></h3>
				<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse">
						<i class="fa fa-minus"></i>
					</button>
				</div>
			</div>
			<div class="box-body">
				<div class="chart">
					<canvas id="saleManagerChart" style="height: 230px; width: 467px;"></canvas>
				</div>
			</div>
			<!-- /.box-body -->
		</div>
		<!-- /.box -->

	</div>
	
	<div class="col-md-6">
		<div class="box">
			<div class="box-header with-border">
				<h3 class="box-title"><spring:message code="report.analyse.device" /></h3>
				<div class="box-tools pull-right">
					<button class="btn btn-box-tool" data-widget="collapse">
						<i class="fa fa-minus"></i>
					</button>
				</div>
			</div>
			<div class="box-body">
				<div class="chart">
					<canvas id="adTypeChart" style="height: 230px; width: 467px;"></canvas>
				</div>
			</div>
			<!-- /.box-body -->
		</div>
		<!-- /.box -->
	</div>

</div>



<script type="text/javascript">
	$(function() {
		$("#analyseCurrency").select2({
            data:currencyData,
            templateResult:templateResult,
            templateSelection:templateResult,
            language: lang_select2()
        });
		
        function templateResult(repo){
            if (repo.loading) return repo.text;
            return  $("<span><img src='${ctx}/static/images/currency/"+repo.text+".gif' class='img-flag' />&nbsp;"+repo.text+"</span>");
        };
		
		// 原始数据
		var analyseDatas = [];
		// 货币符号
		var analyseCurrencySymbol="¥";
		// 货币
		var analyseCurrencyName="RMB";
		// 汇率
		var analyseCurrencyRate=1;
		// 设备报表 pc mobile 需要单独记录
		var adTypeData = {
				data:[{ name:"pc","income":0,"gp":0 },
				      { name:"Mobile","income":0,"gp":0 },
				      { name:"Other","income":0,"gp":0 }
				]}
		var analyseCurrencySymbols={symbols:[{name:"RMB",symbol:"¥"},
		                               {name:"AUD",symbol:"A$"},
		                               {name:"EUR",symbol:"€"},
		                               {name:"GBP",symbol:"£"},
		                               {name:"HKD",symbol:"HK$"},
		                               {name:"IDR",symbol:"Rp"},
		                               {name:"JPY",symbol:"JPY¥"},
		                               {name:"KRW",symbol:"₩"},
		                               {name:"MYR",symbol:"RM"},
		                               {name:"RUB",symbol:"₽"},
		                               {name:"SGD",symbol:"S$"},
		                               {name:"THB",symbol:"฿"},
		                               {name:"TWD",symbol:"NT$"},
		                               {name:"USD",symbol:"$"}
		                               ]};
		
		$("#analyseCurrency").change(function(){
			var symbolData = getAnalyseCurrencySymbol($(this).find("option:selected").text());
			analyseCurrencySymbol = symbolData.symbol;
			analyseCurrencyName = symbolData.name;
			analyseCurrencyRate = $(this).val();
			
			syncCharts();
			
		});
		
		//default value
		$('#reportDateSpan_2').html(moment().startOf('quarter').format('YYYY/MM/DD') + ' - '+ moment().endOf('quarter').format('YYYY/MM/DD'));
		$('#reportDate_2').val(moment().startOf('quarter').format('YYYY/MM/DD') + ' - '+ moment().endOf('quarter').format('YYYY/MM/DD'));
		$('#reportDateDiv_2').daterangepicker(
				{ranges : {
						'<spring:message code="date.today"/>' : [ moment(), moment() ],
						'<spring:message code="date.yesterday"/>' : [ moment().subtract(1, 'days'),moment().subtract(1, 'days') ],
						'<spring:message code="date.last7Days"/>' : [ moment().subtract(6, 'days'),moment() ],
						'<spring:message code="date.last30Days"/>' : [ moment().subtract(29, 'days'),moment() ],
						'<spring:message code="date.thisMonth"/>' : [ moment().startOf('month'),moment().endOf('month') ],
						'<spring:message code="date.lastMonth"/>' : [ moment().subtract(1, 'month').startOf('month'),moment().subtract(1, 'month').endOf('month') ],
						'<spring:message code="date.thisQuarter"/>' : [ moment().subtract(0, 'quarter').startOf('quarter'),moment().subtract(0, 'quarter').endOf('quarter') ],
						'<spring:message code="date.lastQuarter"/>' : [ moment().subtract(1, 'quarter').startOf('quarter'),moment().subtract(1, 'quarter').endOf('quarter') ],
						'<spring:message code="date.nextQuarter"/>' : [ moment().add(1, 'quarter').startOf('quarter'),moment().add(1, 'quarter').endOf('quarter') ]
					},
					format : 'YYYY/MM/DD',
				    locale :{
		                applyLabel: '<spring:message code="date.apply"/>',
		                cancelLabel: '<spring:message code="date.cancel"/>',
		                fromLabel: '<spring:message code="date.from"/>',
		                toLabel: '<spring:message code="date.to"/>',
		                weekLabel: 'W',
		                customRangeLabel: '<spring:message code="date.customRange"/>',
		                daysOfWeek: moment.weekdaysMin(),
		                monthNames: moment.monthsShort(),
		                firstDay: moment.localeData()._week.dow
		            },
					startDate : moment().startOf('quarter'),
					endDate : moment().endOf('quarter')
				},
				function(start, end) {
					$('#reportDateSpan_2').html(start.format('YYYY/MM/DD') + ' - '+ end.format('YYYY/MM/DD'));
					$('#reportDate_2').val(start.format('YYYY/MM/DD') + ' - '+ end.format('YYYY/MM/DD'));
				}
		);
		
		//date change
		$('#reportDateDiv_2').on('apply.daterangepicker', function(ev, picker) {
			//alert(picker.startDate.format('YYYY/MM/DD'));
			//alert(picker.endDate.format('YYYY/MM/DD'));
			
			//saleGroupChart.data.datasets[0].data[2] = 50;
			//saleGroupChart.data.datasets[0].label = '收入';
			
			
			var date =picker.startDate.format('YYYY/MM/DD') +" - "+picker.endDate.format('YYYY/MM/DD');
			loadChartjs(date,analyseCurrencyName);
			
		});
		
		
		var loadChartjs = function(daterange,currency) {
			$.ajax({
				  url: "${ctx}/ajax/analyse",
				  dataType:"json",
				  data:{reportDate:daterange},
				  method: "POST"
				}).done(function ( data ) {
					analyseDatas = data;
					syncCharts();
				});
		} 
		
		
		
		//-------------
		//- 销售团队 -
		//-------------
	    var saleGroupChartData = {
			labels : [],
			datasets : [ {
				label : '<spring:message code="report.analyse.sale" />',
				data : [],
				backgroundColor : 'rgba(45,120,176,0.8)'
			}, {
				label : '<spring:message code="report.analyse.gp" />',
				hidden : gpHidden,
				data : [],
				backgroundColor : 'rgba(0,178,238,0.8)'
			} ]
		};
		
		var ctx = document.getElementById("saleGroupChart").getContext("2d");
		var saleGroupChart = new Chart(
				ctx,
				{
					type : 'bar',
					data : saleGroupChartData,
					options : {
						scales : {
							yAxes : [ {
								ticks : {
									beginAtZero : true,
									userCallback : function(tick) {
										return analyseCurrencySymbol+formatNumber(tick, 0, ',');
									}
								}
							} ]
						},
						legend : {
							display : true,
							labels : {
								fontColor : 'rgb(0,0,0)'
							},
							onClick : function(event, legendItem) {
								var index = legendItem.datasetIndex;

								var ci = this.chart;
								var meta = ci.getDatasetMeta(index);
								//屏蔽预估毛利
								if (index == 1 && gpHidden == true) {
									meta.hidden = true;
								} else {
									// See controller.isDatasetVisible comment
									meta.hidden = meta.hidden === null ? !ci.data.datasets[index].hidden: null;
								}
								// We hid a dataset ... rerender the chart
								ci.update();
							}
						},
						tooltips : {
							callbacks : {
								label : function(tooltipItem, data) {
									var datasetLabel = data.datasets[tooltipItem.datasetIndex].label || '';
									return datasetLabel+ ': '+analyseCurrencySymbol+ formatNumber(tooltipItem.yLabel,0, ',');
								}
							}
						}
					}
				});
		
		

		//-------------
		//- 收入管理阶段 -
		//-------------
	    var incomeManageChartData = {
			labels : [ '<spring:message code="report.analyse.sale.opportunity" />','<spring:message code="report.clo1" />','<spring:message code="report.analyse.campaigns" />' ],
			datasets : [ {
				label : '<spring:message code="report.analyse.sale" />',
				backgroundColor : 'rgba(45,120,176,0.8)',
				data : [  ]
			}, {
				label : '<spring:message code="report.analyse.gp" />',
				hidden : gpHidden,
				backgroundColor : 'rgba(0,178,238,0.8)',
				data : [  ]
			} ]
		};
		var ctx2 = document.getElementById("saleManagerChart").getContext("2d");
		var saleManagerChart = new Chart(
				ctx2,
				{
					type : 'horizontalBar',
					data : incomeManageChartData,
					options : {
						tooltips : {
							mode : 'label',
							callbacks : {
								label : function(tooltipItem, data) {
									var datasetLabel = data.datasets[tooltipItem.datasetIndex].label || '';
									return datasetLabel+ ': '+analyseCurrencySymbol+ formatNumber(tooltipItem.xLabel,0, ',');
								}
							}
						},
						responsive : true,
						legend : {
							position : 'right',
							onClick : function(event, legendItem) {
								var index = legendItem.datasetIndex;

								var ci = this.chart;
								var meta = ci.getDatasetMeta(index);
								//屏蔽预估毛利
								if (index == 1 && gpHidden == true) {
									meta.hidden = true;
								} else {
									// See controller.isDatasetVisible comment
									meta.hidden = meta.hidden === null ? !ci.data.datasets[index].hidden: null;
								}
								// We hid a dataset ... rerender the chart
								ci.update();
							}
						},
						scales : {
							xAxes : [ {
								ticks : {
									beginAtZero : true,
									userCallback : function(tick) {
										return analyseCurrencySymbol+formatNumber(tick, 0, ',');
									}
								}
							} ]
						},
						tooltips : {
                            callbacks : {
                                label : function(tooltipItem, data) {
                                    var datasetLabel = data.datasets[tooltipItem.datasetIndex].label || '';
                                    return datasetLabel+ ': '+analyseCurrencySymbol+ formatNumber(tooltipItem.xLabel,0, ',');
                                }
                            }
                        }

					}
				});
		
		
		
		//-------------
		//- 产品 -
		//-------------
	    var productChartData = {
			labels : [],
			datasets : [ {
				label : '<spring:message code="report.analyse.sale" />',
				data : [],
				backgroundColor : 'rgba(45,120,176,0.8)'
			}, {
				label : '<spring:message code="report.analyse.gp" />',
				hidden : gpHidden,
				data : [],
				backgroundColor : 'rgba(0,178,238,0.8)'
			} ]
		};
		var ctx3 = document.getElementById("productChart").getContext("2d");
		var productChart = new Chart(
				ctx3,
				{
					type : 'bar',
					data : productChartData,
					options : {
						scales : {
							yAxes : [ {
								ticks : {
									beginAtZero : true,
									userCallback : function(tick) {
										return analyseCurrencySymbol+formatNumber(tick, 0, ',');
									}
								}
							} ]
						},
						legend : {
							display : true,
							labels : {
								fontColor : 'rgb(0,0,0)'
							},
							onClick : function(event, legendItem) {
								var index = legendItem.datasetIndex;

								var ci = this.chart;
								var meta = ci.getDatasetMeta(index);
								//屏蔽预估毛利
								if (index == 1 && gpHidden == true) {
									meta.hidden = true;
								} else {
									// See controller.isDatasetVisible comment
									meta.hidden = meta.hidden === null ? !ci.data.datasets[index].hidden: null;
								}
								// We hid a dataset ... rerender the chart
								ci.update();
							}
						},
						tooltips : {
							callbacks : {
								label : function(tooltipItem, data) {
									var datasetLabel = data.datasets[tooltipItem.datasetIndex].label || '';
									return datasetLabel+ ': '+analyseCurrencySymbol+ formatNumber(tooltipItem.yLabel,0, ',');
								}
							}
						}
					}
				});
		
		
		
		
		//-------------
		//- 设备 -
		//-------------
	    var adTypeChartData = {
			labels : [ ],
			datasets : [ {
				label : '',
				data : [ ],
				backgroundColor: [
				                  'rgba(111,197,187,0.5)',
				                  'rgba(243,197,83,0.5)'
				              ]
			}]
		};
		var ctx4 = document.getElementById("adTypeChart").getContext("2d");
		var adTypeChart = new Chart(
				ctx4,
				{
					type : 'pie',
					data : adTypeChartData,
					options : {
						tooltips : {
							callbacks : {
								label : function(tooltipItem, data) {
									//var datasetLabel = data.datasets[tooltipItem.datasetIndex].label || '';
									var adTypeLabel = "";
									var incomeLabel = "<spring:message code='report.income' />";
									var gpLable = "<spring:message code='report.analyse.gp' />";
									var index = tooltipItem.index;
									
									adTypeLabel = incomeLabel+":"+analyseCurrencySymbol+formatNumber(adTypeData.data[index].income,0,',');
									if(analyseDatas.gpQuery==true){
										adTypeLabel = adTypeLabel+"; "+gpLable+":"+analyseCurrencySymbol+formatNumber(adTypeData.data[index].gp,0,',');
									}
									
									//adTypeData.data[0].income
									return adTypeLabel;
								}
							}
						}
					}
				});
		
		
		//数字格式化
		function formatNumber(num, precision, separator) {
			var parts;
			// 判断是否为数字
			if (!isNaN(parseFloat(num)) && isFinite(num)) {
				// 把类似 .5, 5. 之类的数据转化成0.5, 5, 为数据精度处理做准, 至于为什么
				// 不在判断中直接写 if (!isNaN(num = parseFloat(num)) && isFinite(num))
				// 是因为parseFloat有一个奇怪的精度问题, 比如 parseFloat(12312312.1234567119)
				// 的值变成了 12312312.123456713
				num = Number(num);
				// 处理小数点位数
				num = (typeof precision !== 'undefined' ? num.toFixed(precision)
						: num).toString();
				// 分离数字的小数部分和整数部分
				parts = num.split('.');
				// 整数部分加[separator]分隔, 借用一个著名的正则表达式
				parts[0] = parts[0].toString().replace(/(\d)(?=(\d{3})+(?!\d))/g,
						'$1' + (separator || ','));

				return parts.join('.');
			}
			return NaN;
		};
		
		function getAnalyseCurrencySymbol(currency){
			for(var i = 0 ; i < analyseCurrencySymbols.symbols.length;i++){
				if(currency == analyseCurrencySymbols.symbols[i].name){
					return analyseCurrencySymbols.symbols[i];
				}
			}
		};
		
		function syncCharts(){
			var data = analyseDatas;
			var rate = analyseCurrencyRate;
			//值
			var val1 = rateChange(rate,data.totalOpportunity);
			var val2 = rateChange(rate,data.totalOrder);
			var val3 = rateChange(rate,data.totalGPOpportunity);
			var val4 = rateChange(rate,data.totalGPOrder);
			// campaign
			var valCampaignSum = rateChange(rate,data.campaignSum);
			
			$("#totalOpportunity").text(analyseCurrencySymbol + formatNumber(val1,0,',')   );
			$("#totalOrder").text(analyseCurrencySymbol + formatNumber(val2,0,','));
			$("#totalGPOpportunity").text(analyseCurrencySymbol + formatNumber(val3,0,','));
			$("#totalGPOrder").text(analyseCurrencySymbol + formatNumber(val4,0,','));
			$("#advertiserCount").text(data.advertiserCount);
			$("#orderCount").text(data.orderCount);
			$("#campaignCount").text(data.campaignCount);
			
			//1
			saleGroupChart.data.labels = data.labels1;
			saleGroupChart.data.datasets[0].data=rateChange(rate,data.dataIncome1);
			saleGroupChart.data.datasets[1].data=rateChange(rate,data.dataGp1);
			saleGroupChart.update();
			
			//2
			var data1 = new Array(0);
			var data2 = new Array(0);
			data1.push(val1);
			data1.push(val2);
			data1.push(valCampaignSum);
			data2.push(val3);
			data2.push(val4);
			//data2.push(0);
			saleManagerChart.data.datasets[0].data=data1;
			saleManagerChart.data.datasets[1].data=data2;
			saleManagerChart.update();
			
			//3
			productChart.data.labels = data.labels3;
			productChart.data.datasets[0].data=rateChange(rate,data.dataIncome3);
			productChart.data.datasets[1].data=rateChange(rate,data.dataGp3);
			productChart.update();
			
			
			//4
			var tmpIncomeData = rateChange(rate,data.dataIncome4);;
			var tmpGpData = rateChange(rate,data.dataGp4);
			
			adTypeChart.data.labels = data.labels4;
			adTypeChart.data.datasets[0].data=tmpIncomeData;
			// 因为这个饼图是一维的,只能有一组数据, 所以要么按照income的比例, 要么按照gp的比例
			//adTypeChart.data.datasets[1].data=tmpGpData;
			adTypeChart.update();
			// 单独处理, 这里赋值, label显示的时候需要拼接income+gp
			adTypeData.data[0].income=tmpIncomeData[0];
			adTypeData.data[0].gp=tmpGpData[0];
			adTypeData.data[1].income=tmpIncomeData[1];
			adTypeData.data[1].gp=tmpGpData[1];
			adTypeData.data[2].income=tmpIncomeData[2];
			adTypeData.data[2].gp=tmpGpData[2];
		};
		
		// 用于数组
		function rateChange(rate,money){
			var _rate = +rate;
			if(isArrayFn(money)){
				var _money = [];
				for(var i = 0 ;i <money.length;i++){
					_money.push((money[i] * _rate).toFixed(2));
				}
				return _money;
			}else{
				return (money*_rate).toFixed(2);
			}
		};
		
		function isArrayFn(value){  
		    if (typeof Array.isArray === "function") {  
		        return Array.isArray(value);      
		    }else{  
		        return Object.prototype.toString.call(value) === "[object Array]";      
		    }  
		};
		
		//第一次点击分析tab时候，ajax请求数据，以后无须请求
		var first = true;
		$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
			  var target = $(e.target).attr("href") // activated tab
			  if(target == '#timeline' && first){
				//页面初始触发，更新chartjs数据
				loadChartjs($('#reportDate_2').val(),analyseCurrencyName);
				// 默认货币
		        $("#analyseCurrency option:contains('<c:out value="${defaultCurrency}"/>')").attr("selected", true);
		        $("#analyseCurrency").change();
				first = false;
			  }
	   });
		
		$("#downloadDashboard").on('click',function(){
			var date = $('#reportDate_2').val();
            var currency = $("#analyseCurrency").find("option:selected").text();
            var data = analyseDatas;
            var rate = analyseCurrencyRate;
            //值
            var val1 = rateChange(rate,data.totalOpportunity);
            var val2 = rateChange(rate,data.totalOrder);
            var val3 = rateChange(rate,data.totalGPOpportunity);
            var val4 = rateChange(rate,data.totalGPOrder);
            // campaign
            var valCampaignSum = rateChange(rate,data.campaignSum);
            // 传到后台数据
            var totalOpportunity = val1;
            var totalOrder = val2;
            var totalGPOpportunity = val3;
            var totalGPOrder = val4;
            var advertiserCount = data.advertiserCount;
            var orderCount = data.orderCount;
            var campaignCount = data.campaignCount;
            
            //收入管理, 这个其实是来自val1234, 已经转了汇率
            var saleManagerData = saleManagerChart.data.datasets[0].data;
            var saleManagerGP = saleManagerChart.data.datasets[1].data;
            
            // 设备
            var adTypeIncomeData = rateChange(rate,data.dataIncome4);;
            var adTypeGpData = rateChange(rate,data.dataGp4);
            
            // 销售团队
            var saleGroupLabels = data.labels1;
            for(var i = 0 ; i<saleGroupLabels.length;i++){
            	saleGroupLabels[i] = saleGroupLabels[i].replace(/\'/g,"’");
            }
            var saleGroupIncomeData = rateChange(rate,data.dataIncome1);
            var saleGroupGpData = rateChange(rate,data.dataGp1);
            
            // 广告形式
            var productLabels = data.labels3;
            for(var i = 0 ; i<productLabels.length;i++){
            	productLabels[i] = productLabels[i].replace(/\'/g,"’");
            }
            var productIncomeData = rateChange(rate,data.dataIncome3);
            var productGpData = rateChange(rate,data.dataGp3);
            
            var inputs = '';
            inputs+='<input type="hidden" name="date" value="'+ date +'" />'; 
            inputs+='<input type="hidden" name="currency" value="'+ currency +'" />'; 
            
            inputs+='<input type="hidden" name="totalOpportunity" value="'+ totalOpportunity +'" />'; 
            inputs+='<input type="hidden" name="totalOrder" value="'+ totalOrder +'" />'; 
            inputs+='<input type="hidden" name="totalGPOpportunity" value="'+ totalGPOpportunity +'" />'; 
            inputs+='<input type="hidden" name="totalGPOrder" value="'+ totalGPOrder +'" />'; 
            inputs+='<input type="hidden" name="advertiserCount" value="'+ advertiserCount +'" />'; 
            inputs+='<input type="hidden" name="orderCount" value="'+ orderCount +'" />'; 
            inputs+='<input type="hidden" name="campaignCount" value="'+ campaignCount +'" />';
            
            inputs+='<input type="hidden" name="saleManagerData" value=\''+ JSON.stringify(saleManagerData) +'\' />'; 
            inputs+='<input type="hidden" name="saleManagerGP" value=\''+ JSON.stringify(saleManagerGP) +'\' />'; 
            inputs+='<input type="hidden" name="adTypeIncomeData" value=\''+ JSON.stringify(adTypeIncomeData) +'\' />'; 
            inputs+='<input type="hidden" name="adTypeGpData" value=\''+ JSON.stringify(adTypeGpData) +'\' />'; 
            inputs+='<input type="hidden" name="saleGroupLabels" value=\''+ JSON.stringify(saleGroupLabels) +'\' />'; 
            inputs+='<input type="hidden" name="saleGroupIncomeData" value=\''+ JSON.stringify(saleGroupIncomeData) +'\' />'; 
            inputs+='<input type="hidden" name="saleGroupGpData" value=\''+ JSON.stringify(saleGroupGpData) +'\' />'; 
            inputs+='<input type="hidden" name="productLabels" value=\''+ JSON.stringify(productLabels) +'\' />'; 
            inputs+='<input type="hidden" name="productIncomeData" value=\''+ JSON.stringify(productIncomeData) +'\' />'; 
            inputs+='<input type="hidden" name="productGpData" value=\''+ JSON.stringify(productGpData) +'\' />'; 
            console.log(inputs);
            $('<form action="${ctx}/report/downloadDashboard" method="post">'+inputs+'</form>').appendTo('body').submit().remove();
            
		});

	});
	
	
</script>
