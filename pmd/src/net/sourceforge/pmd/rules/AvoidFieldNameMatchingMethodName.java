/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package net.sourceforge.pmd.rules;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.pmd.AbstractRule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.ast.ASTInterfaceDeclaration;
import net.sourceforge.pmd.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.ast.ASTUnmodifiedClassDeclaration;
import net.sourceforge.pmd.ast.SimpleNode;



public class AvoidFieldNameMatchingMethodName extends AbstractRule {
	
	public Object visit(ASTInterfaceDeclaration node, Object data) {
		// Skip interfaces
		return super.visit(node, data);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.pmd.ast.JavaParserVisitor#visit(net.sourceforge.pmd.ast.ASTFieldDeclaration, java.lang.Object)
	 */
	public Object visit(ASTFieldDeclaration node, Object data) {
		String varName = node.getVariableName();
		String fieldDeclaringType = getDeclaringType (node);
		if (varName!=null) {
			varName = varName.toLowerCase();
			//Get parent type
			ASTUnmodifiedClassDeclaration cl = (ASTUnmodifiedClassDeclaration) node.getFirstParentOfType(ASTUnmodifiedClassDeclaration.class);
			
			List methods = cl.findChildrenOfType(ASTMethodDeclaration.class);
			if (methods!=null && !methods.isEmpty()) {
				for (Iterator it = methods.iterator() ; it.hasNext() ; ) {
					ASTMethodDeclaration m = (ASTMethodDeclaration) it.next();
					//Make sure we are comparing fields and methods inside same type
					if (fieldDeclaringType.equals(getDeclaringType(m))) {
						String n = m.getMethodName();
						if (n!=null && varName.equals(n.toLowerCase())) {
							addViolation((RuleContext) data, node.getBeginLine());
						}
					}
				}
			}
		}
		return data;
	}
	private String getDeclaringType(SimpleNode  node) {
		ASTUnmodifiedClassDeclaration c = (ASTUnmodifiedClassDeclaration) node.getFirstParentOfType(ASTUnmodifiedClassDeclaration.class);
		if (c!=null)
			return c.getImage();
		return null;
	}
}
