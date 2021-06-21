<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${sessionScope.loggedUser.role != 'USER'}">
    <a href="<c:url value="/privateOffice"/>" class="btn btn-outline-info btn-sm">
            ${sessionScope.loggedUser.name} ${sessionScope.loggedUser.surname}
    </a>
    <a href="<c:url value="/signOut"/>" class="btn btn-outline-dark btn-sm">
        <fmt:message key="sign.out"/>
    </a>
</c:if>