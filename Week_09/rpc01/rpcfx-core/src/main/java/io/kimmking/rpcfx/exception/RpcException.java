package io.kimmking.rpcfx.exception;

public class RpcException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private String code;

    public RpcException(){
        super();
    }
    public RpcException(String errorCode,String errMsg){
        super(errMsg);
        code=errorCode;
    }
    public String getCode() {
        return code;
    }
}
