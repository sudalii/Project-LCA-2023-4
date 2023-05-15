<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>LCA Project</title>

    <!-- Custom fonts for this template-->
    <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="vendor/fontawesome-free/css/fontawesome.min.css" rel="stylesheet" type="text/css">
    <link href="vendor/fontawesome-free/css/solid.min.css" rel="stylesheet" type="text/css">
    <link
        href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
        rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="css/sb-admin-2.css?v=1" rel="stylesheet">

    <!-- Custom styles for this page -->
    <link href="vendor/datatables/dataTables.bootstrap4.css" rel="stylesheet">

</head>

<body id="page-top">

    <!-- Page Wrapper -->
    <div id="wrapper">

        <!-- Sidebar -->
        <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

            <!-- Sidebar - Brand -->
            <a class="sidebar-brand d-flex align-items-center justify-content-center" href="index.html">
                <div class="sidebar-brand-icon">
                    <img src="img/keti_logo_119x25.jpg">
                    <!--<i class="fas fa-laugh-wink"></i>-->
                </div>
                <div class="sidebar-brand-text mx-3">User Input</div>
            </a>

            <!-- Divider -->
            <hr class="sidebar-divider my-0">

            <!-- Nav Item - Project Management -->
            <li class="nav-item">
                <a class="nav-link" href="project-list">
                    <i class="fas fa-fw fa-tachometer-alt"></i>
                    <span>Project Management</span></a>
            </li>

            <!-- Nav Item - Project overview -->
            <li class="nav-item">
                <a class="nav-link" href="project-overview.html">
                    <i class="fas fa-fw fa-tachometer-alt"></i>
                    <span>Project Overview</span></a>
            </li>            

            <!-- Divider -->
            <hr class="sidebar-divider">

            <!-- Heading -->
            <div class="sidebar-heading">
                Project Infomation
            </div>

            <!-- Nav Item - Tables -->
            <li class="nav-item active">
                <a class="nav-link" href="project-info.jsp">
                    <i class="fas fa-fw fa-table"></i>
                    <span>Project <br>&emsp;&ensp; General Infomation</span>
                    </a>
            </li>

            <!-- Divider -->
            <hr class="sidebar-divider d-none d-md-block">

            <!-- Sidebar Toggler (Sidebar) -->
            <div class="text-center d-none d-md-inline">
                <button class="rounded-circle border-0" id="sidebarToggle"></button>
            </div>

        </ul>
        <!-- End of Sidebar -->

        <!-- Content Wrapper -->
        <div id="content-wrapper" class="d-flex flex-column">

            <!-- Main Content -->
            <div id="content">

                <!-- Topbar -->
                <nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">

                    <!-- Sidebar Toggle (Topbar) -->
                    <button id="sidebarToggleTop" class="btn btn-link d-md-none rounded-circle mr-3">
                        <i class="fa fa-bars"></i>
                    </button>

                    <!-- Topbar Search -->
                    <form
                        class="d-none d-sm-inline-block form-inline mr-auto ml-md-3 my-2 my-md-0 mw-100 navbar-search">
                        <div class="input-group">
                            <input type="text" class="form-control bg-light border-0 small" placeholder="Search for..."
                                aria-label="Search" aria-describedby="basic-addon2">
                            <div class="input-group-append">
                                <button class="btn btn-primary" type="button">
                                    <i class="fas fa-search fa-sm"></i>
                                </button>
                            </div>
                        </div>
                    </form>

                    <!-- Topbar Navbar -->
                    <ul class="navbar-nav ml-auto">

                        <!-- Nav Item - Search Dropdown (Visible Only XS) -->
                        <li class="nav-item dropdown no-arrow d-sm-none">
                            <a class="nav-link dropdown-toggle" href="#" id="searchDropdown" role="button"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="fas fa-search fa-fw"></i>
                            </a>
                            <!-- Dropdown - Messages -->
                            <div class="dropdown-menu dropdown-menu-right p-3 shadow animated--grow-in"
                                aria-labelledby="searchDropdown">
                                <form class="form-inline mr-auto w-100 navbar-search">
                                    <div class="input-group">
                                        <input type="text" class="form-control bg-light border-0 small"
                                            placeholder="Search for..." aria-label="Search"
                                            aria-describedby="basic-addon2">
                                        <div class="input-group-append">
                                            <button class="btn btn-primary" type="button">
                                                <i class="fas fa-search fa-sm"></i>
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </li>

                        <div class="topbar-divider d-none d-sm-block"></div>

                        <!-- Nav Item - User Information -->
                        <li class="nav-item dropdown no-arrow">
                            <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <span class="mr-2 d-none d-lg-inline text-gray-600 small">KETI</span>
                                <img class="img-profile rounded-circle"
                                    src="img/undraw_profile.svg">
                            </a>
                            <!-- Dropdown - User Information -->
                            <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in"
                                aria-labelledby="userDropdown">
                                <a class="dropdown-item" href="#">
                                    <i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
                                    Profile
                                </a>
                                <a class="dropdown-item" href="#">
                                    <i class="fas fa-cogs fa-sm fa-fw mr-2 text-gray-400"></i>
                                    Settings
                                </a>
                                <a class="dropdown-item" href="#">
                                    <i class="fas fa-list fa-sm fa-fw mr-2 text-gray-400"></i>
                                    Activity Log
                                </a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" href="#" data-toggle="modal" data-target="#logoutModal">
                                    <i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
                                    Logout
                                </a>
                            </div>
                        </li>

                    </ul>

                </nav>
                <!-- End of Topbar -->

                <!-- Begin Page Content -->
                <div class="container-fluid">

                    <main class="main">
                        <form action="#" method="GET" id="survey-form" class="survey" novalidate>
                            <!--<c:forEach items="${voList}" var="vo">-->
    
                                <div class="process-wrap active-step6" tabindex="0" role="progressbar" aria-valuemin="1" aria-valuemax="6" aria-valuenow="1" aria-valuetext="Question 1 of 5: How long have you been using our product?">
                                    <div class="row my-row">
                                        <div class="col-2 my-col">
                                            <div class="process-step-cont">
                                                <div class="process-step step-1"></div>
                                                <span class="process-label">프로젝트<br>정보입력</span>
                                            </div>
                                        </div>
                                        <div class="col-2 my-col">
                                            <div class="process-step-cont">
                                                <div class="process-step step-2"></div>
                                                <span class="process-label">물질 및<br>부품제조</span>
                                            </div>
                                        </div>
                                        <div class="col-2 my-col">
                                            <div class="process-step-cont">
                                                <div class="process-step step-3"></div>
                                                <span class="process-label">가공공정</span>
                                            </div>
                                        </div>
                                        <div class="col-2 my-col">
                                            <div class="process-step-cont">
                                                <div class="process-step step-4"></div>
                                                <span class="process-label">수송</span>
                                            </div>
                                        </div>
                                        <div class="col-2 my-col">
                                            <div class="process-step-cont">
                                                <div class="process-step step-5"></div>
                                                <span class="process-label">재활용 및<br>폐기</span>
                                            </div>
                                        </div>
                                        <div class="col-2 my-col">
                                            <div class="process-step-cont">
                                                <div class="process-step step-6"></div>
                                                <span class="process-label">GWP<br>결과산출</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="mt-2">
                                    
                                    <a class="d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm" style="float:right;">
                                        <i class="fas fa-download fa-sm text-white-50"></i> Generate Report</a>

                                    <a id="ping-test" class="d-none d-sm-inline-block btn btn-sm btn-success shadow-sm mr-2" style="float:right;" 
                                        onclick="pingURL()">플랫폼 응답시간 Test</a>
                                </div>

                                <div class="row mt-1">
                                    <!-- -->
                                    <div class="card my-card1 shadow mb-2 col-lg-12">
                                        <div class="card-body">
                                            <h6 class="mt-1 font-weight-bold text-primary">${vo.projectName}</h6>
                                            <h6 class="mt-2 font-weight-bold">${vo.productName}, 기준 양: ${vo.productAmount} kg</h6>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- Content Row -->
                                <div class="row"> 

                                    <!-- LCIA Select -->
                                    <div class="card my-card1 shadow col-lg-12 mb-2">

                                        <div class="card-body">
                                            <h6 class="mt-2 font-weight-bold text-primary">환경영향범주 선택</h6>
                                            <div class="dropdown" class="form-control form-control-user">
                                                <select name="lcia-method" id="dropdown" class="form-control">
                                                    <option value="GWP">지구온난화(GWP)</option>
                                                </select>
                                            </div>

                                            <h6 class="mt-4 font-weight-bold text-primary">Impact analysis: Table</h6>
                                            <div class="table-responsive col-sm-12">
                                                <table class="table table-bordered" id="impact_analysis" width="100%" cellspacing="0">                                                
                                                    <thead>
                                                        <tr>
                                                            <th>구분</th>                                                  
                                                            <th>Inventory result</th>
                                                            <th>Characterization factor</th>
                                                            <th>Impact assessment result</th>
                                                            <th>단위</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <tr>
                                                            <td>CO2</td>
                                                            <td>${vo.lciResult1} (kg)</td>
                                                            <td>${vo.charFactor1} (kg CO2 eq/kg)</td>
                                                            <td>${vo.flowLciaResult1}</td>
                                                            <td>${vo.resultUnit}</td>
                                                        </tr>
                                                        <tr>
                                                            <td>Methane</td>
                                                            <td>${vo.lciResult2} (kg)</td>
                                                            <td>${vo.charFactor2} (kg CO2 eq/kg)</td>
                                                            <td>${vo.flowLciaResult2}</td>
                                                            <td>${vo.resultUnit}</td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <h6 class="font-weight-bold text-danger" style="display:inline">➜</h6>
                                            <h6 class="font-weight-bold text-danger" style="display:inline"> 총 환경영향: </h6>
                                            <h6 class="font-weight-bold text-danger" style="display:inline"> ${vo.result} ${vo.resultUnit}</h6>
                                            
                                            
                                            <h6 class="mt-4 font-weight-bold text-primary">Impact analysis: Graphs</h6>
                                            <h6 class="mt-2 font-weight-bold">그래프 삽입 예정</h6>
                                            <div class="col-5" style="display:inline">
                                                <div class="chart-pie pt-4 pb-2">
                                                    <canvas id="myPieChart2"></canvas>
                                                </div>
                                            </div>
                                            <div class="col-5" style="display:inline">
                                                <div class="chart-pie pt-4 pb-2">
                                                    <canvas id="myBarChart2"></canvas>
                                                </div>
                                            </div>                                                
                                        </div>
                                    </div> 
                                    <!-- LCIA Select End -->
                                </div>    
                                        
                                <div class="row">         
                                    <div class="card my-card1 shadow mb-2 col-lg-12">
                                        <div class="card-body">
                                            <h6 class="mt-1 font-weight-bold text-primary">openLCA - KETI Web 비교</h6>
                                            <h6 class="mt-2 font-weight-bold">openLCA의 총 환경영향: ${vo.result2} ${vo.resultUnit}</h6>
                                            <h6 class="mt-2 font-weight-bold">➜ 정확도: ${vo.percent} %</h6>
                                            <div class="col-auto">
                                                <div class="chart-pie pt-4 pb-2">
                                                    <canvas id="myBarChart1"></canvas>
                                                </div>
                                            </div>
                                        </div>
                                    </div>              	
                                </div>   
                                <div class="row">         
                                    <div class="card my-card1 shadow mb-2 col-lg-12">
   
                                    </div>
                                </div>      	                    				                     
                            </c:forEach>
                        </form>
                    </main>
                </div>
                <!-- /.container-fluid -->

            </div>
            <!-- End of Main Content -->

            <!-- Footer -->
            <footer class="sticky-footer bg-white">
                <div class="container my-auto">
                    <div class="copyright text-center my-auto">
                        <!--<span>Copyright &copy; Your Website 2020</span>-->
                    </div>
                </div>
            </footer>
            <!-- End of Footer -->

        </div>
        <!-- End of Content Wrapper -->

    </div>
    <!-- End of Page Wrapper -->

    <!-- Scroll to Top Button-->
    <a class="scroll-to-top rounded" href="#page-top">
        <i class="fas fa-angle-up"></i>
    </a>

    <!-- Logout Modal-->
    <div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
        aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">��</span>
                    </button>
                </div>
                <div class="modal-body">Select "Logout" below if you are ready to end your current session.</div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                    <a class="btn btn-primary" href="login.html">Logout</a>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap core JavaScript-->
    <script src="vendor/jquery/jquery.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

    <!-- Core plugin JavaScript-->
    <script src="vendor/jquery-easing/jquery.easing.min.js"></script>

    <!-- Custom scripts for all pages-->
    <script src="js/sb-admin-2.js?v=1"></script>

    <!-- Page level plugins -->
    <script src="vendor/datatables/jquery.dataTables.js"></script>
    <script src="vendor/datatables/dataTables.bootstrap4.js"></script>
    <!-- Page level plugins -->
    <script src="vendor/chart.js/Chart.min.js"></script>

    <!-- Page level custom scripts -->
    <script src="js/demo/chart-bar-demo.js?v=2"></script>
    <script src="js/demo/chart-pie-demo.js?v=2"></script>
    <script src="js/demo/datatables-demo.js"></script>

    <!-- Ping test -->
    <script src="js/ping-function.js"></script>

</body>

</html>