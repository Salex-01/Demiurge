import java.security.NoSuchAlgorithmException;

public class Test {
	public static void main(String[] args) throws NoSuchAlgorithmException {
		System.out.println(EncryptionManager.encrypt("null","SHA3-256"));
	}
}