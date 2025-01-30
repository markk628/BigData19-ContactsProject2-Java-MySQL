package mvc.model.contact;

import java.util.ArrayList;

/**
 * @packageName : mvc.model.contact
 * @fileNmae	: DataAccessObjectInterface.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : DAO Interface
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public interface DataAccessObjectInterface {
	public ContactDTO insert();
	public ArrayList<ContactDTO> select(String name);
	public ArrayList<ContactDTO> select();
	public ContactDTO update();
	public ContactDTO delete();
}
