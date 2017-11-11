package org.kucro3.keleton;

import org.kucro3.annotation.Since;

import java.util.UUID;

/**
 * 
 * @author Kumonda221
 *
 */
@Since(majorVersion = 1, minorVersion = 0)
public interface UniqueService {
	/**
	 * ��ô˷�������UUID��
	 * <br />��ע�⣺��UUID�������������ڶ���־û����龳��UUID��ʵ�������ɵģ�������������ɵġ�
	 * ��UUID�����ý���������ÿ����������Ψһ�ԣ��ҷ�����뱣֤�ڷ�����󱻹���֮��UUID����仯��ֱ���÷�������������������գ�
	 * @return �˵�½�����UUID
	 */
	@Since(majorVersion = 1, minorVersion = 0)
	public UUID getUniqueId();
}
